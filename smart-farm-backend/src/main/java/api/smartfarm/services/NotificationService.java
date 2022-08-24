package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.documents.notifications.NotificationStatus;
import api.smartfarm.models.documents.notifications.SensorFailNotification;
import api.smartfarm.models.documents.notifications.SmartFarmNotification;
import api.smartfarm.models.dtos.notifications.NotificationDTO;
import api.smartfarm.models.dtos.notifications.SensorFailNotificationDTO;
import api.smartfarm.models.exceptions.InvalidNotificationException;
import api.smartfarm.repositories.NotificationDAO;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(
        NotificationDAO notificationDAO,
        UserService userService
    ) {
        this.notificationDAO = notificationDAO;
        this.userService = userService;
    }

    public void sendSensorFailNotification(
        String sensorCode,
        Farm farm
    ) {
        User user = userService.getUserById(farm.getUserId());

        Notification notification = Notification.builder()
            .setTitle("Sensor " + sensorCode + " fallando.")
            .setBody("El sensor " + sensorCode + " esta fallando, favor de revisar la conexión.")
            .build();

        MulticastMessage message = MulticastMessage.builder()
            .setNotification(notification)
            .addAllTokens(user.getDeviceIds())
            .build();

        SmartFarmNotification sfNotification = new SensorFailNotification(
            new Date(),
            farm.getId(),
            user.getId(),
            user.getDeviceIds(),
            sensorCode
        );

        boolean sendSuccessfully = false;

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            for (SendResponse sr : response.getResponses()) {
                if (sr.isSuccessful()) {
                    sendSuccessfully = true;
                    sfNotification.getMessageIds().add(sr.getMessageId());
                } else {
                    LOGGER.error("Failed to send push notification to userId {}, error: {}", user.getId(), sr.getException().getMessage());
                }
            }
        } catch (FirebaseMessagingException e) {
            LOGGER.error("Could not send sensor alert notification to userId {}", user.getId());
        }

        if (sendSuccessfully) {
            sfNotification.setStatus(NotificationStatus.SENT);
        } else {
            sfNotification.setStatus(NotificationStatus.FAIL);
        }

        sfNotification = notificationDAO.save(sfNotification);
        LOGGER.info("Notification created with id {}", sfNotification.getId());
    }

    public List<NotificationDTO> getNotifications(String farmId) {
        List<SmartFarmNotification> notifications = notificationDAO.findByFarmId(farmId);

        return notifications.stream().map(it -> {
            if (it instanceof SensorFailNotification) {
                SensorFailNotification sensorFailNotification = (SensorFailNotification) it;
                return new SensorFailNotificationDTO(sensorFailNotification);
            }
            throw new InvalidNotificationException("NotificationDTO " + it.getClass().getSimpleName() + " not supported");
        }).collect(Collectors.toList());
    }
}
