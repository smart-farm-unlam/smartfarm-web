package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.documents.notifications.*;
import api.smartfarm.models.dtos.notifications.NotificationDTO;
import api.smartfarm.models.dtos.notifications.SensorFailNotificationDTO;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.repositories.NotificationDAO;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public boolean notificationSentRecently(String farmId, long minutesToCheck, NotificationType type) {
        Optional<ParameterOutOfRangeNotification> lastNotification = notificationDAO
            .findTop1ByFarmIdAndTypeAndStatusOrderByDateDesc(
                farmId,
                type.getDescription(),
                NotificationStatus.SENT
            );
        if (!lastNotification.isPresent())
            return false;

        Date currentDate = new Date();
        long minutesAfterLastNotification = ChronoUnit.MINUTES.between(lastNotification.get().getDate().toInstant(), currentDate.toInstant());
        return minutesAfterLastNotification < minutesToCheck;
    }

    public void sendSensorFailNotification(
        String sensorCode,
        Farm farm
    ) {
        User user = userService.getUserById(farm.getUserId());

        String sectorCode = "";
        Optional<Sector> sectorRetrieve = farm.getSectorBySensorCode(sensorCode);
        if (sectorRetrieve.isPresent()) {
            sectorCode = sectorRetrieve.get().getCode();
        }

        String title = String.format("Sensor %s fallando", sectorCode);
        String body = String.format("El sensor %s esta fallando, favor de revisar la conexión.", sectorCode);
        Notification notification = buildNotification(title, body);
        MulticastMessage message = buildMessage(notification, sectorCode, sensorCode, user);
        SmartFarmNotification sfNotification = buildSensorFailNotification(farm, user, sensorCode, title, body);

        sfNotification.setStatus(sendPushNotification(message, sfNotification, user));
        saveNotification(sfNotification);
    }

    public void sendParameterOutOfRange(
        String body,
        Farm farm
    ) {

        User user = userService.getUserById(farm.getUserId());

        String title = "Hay parámetros fuera de lo normal en tu huerta!";
        Notification notification = buildNotification(title, body);
        MulticastMessage message = buildMessage(notification, user);
        SmartFarmNotification sfNotification = buildParameterOutOfRangeNotification(farm, user, title, body);

        sfNotification.setStatus(sendPushNotification(message, sfNotification, user));
        saveNotification(sfNotification);
    }

    public void sendWeatherAlertCondition(
        String title,
        String body,
        Farm farm
    ) {
        User user = userService.getUserById(farm.getUserId());

        Notification notification = buildNotification(title, body);
        MulticastMessage message = buildMessage(notification, user);
        SmartFarmNotification sfNotification = buildWeatherAlertNotification(farm, user, title, body);

        sfNotification.setStatus(sendPushNotification(message, sfNotification, user));
        saveNotification(sfNotification);
    }

    private SensorFailNotification buildSensorFailNotification(
        Farm farm,
        User user,
        String sensorCode,
        String title,
        String body
    ) {
        return new SensorFailNotification(
            new Date(),
            farm.getId(),
            user.getId(),
            user.getDeviceIds(),
            sensorCode,
            title,
            body
        );
    }

    private ParameterOutOfRangeNotification buildParameterOutOfRangeNotification(
        Farm farm,
        User user,
        String title,
        String body
    ) {
        return new ParameterOutOfRangeNotification(
            new Date(),
            farm.getId(),
            user.getId(),
            user.getDeviceIds(),
            title,
            body
        );
    }

    private WeatherAlertNotification buildWeatherAlertNotification(
        Farm farm,
        User user,
        String title,
        String body
    ) {
        return new WeatherAlertNotification(
            new Date(),
            farm.getId(),
            user.getId(),
            user.getDeviceIds(),
            title,
            body
        );
    }

    private Notification buildNotification(String title, String body) {
        return Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build();
    }

    private MulticastMessage buildMessage(Notification notification, String sectorCode, String sensorCode, User user) {
        return MulticastMessage.builder()
            .setNotification(notification)
            .putData("sectorCode", sectorCode)
            .putData("sensorCode", sensorCode)
            .addAllTokens(user.getDeviceIds())
            .build();
    }

    private MulticastMessage buildMessage(Notification notification, User user) {
        return MulticastMessage.builder()
            .setNotification(notification)
            .addAllTokens(user.getDeviceIds())
            .build();
    }

    private void saveNotification(SmartFarmNotification sfNotification) {
        sfNotification = notificationDAO.save(sfNotification);
        LOGGER.info("Notification created with id {}", sfNotification.getId());
    }

    private NotificationStatus sendPushNotification(MulticastMessage message, SmartFarmNotification sfNotification, User user) {
        boolean sendSuccessfully = false;

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            for (SendResponse sr : response.getResponses()) {
                if (sr.isSuccessful()) {
                    sendSuccessfully = true;
                    sfNotification.addMessageId(sr.getMessageId());
                } else {
                    LOGGER.error("Failed to send push notification to userId {}, error: {}", user.getId(), sr.getException().getMessage());
                }
            }
        } catch (FirebaseMessagingException e) {
            LOGGER.error("Could not send sensor alert notification to userId {}", user.getId());
        }

        if (sendSuccessfully) {
            return NotificationStatus.SENT;
        } else {
            return NotificationStatus.FAIL;
        }
    }

    public List<NotificationDTO> getNotifications(String farmId) {
        List<SmartFarmNotification> notifications = notificationDAO.findByFarmIdAndStatus(farmId, NotificationStatus.SENT);

        return notifications.stream().map(it -> {
            if (it instanceof SensorFailNotification) {
                SensorFailNotification sensorFailNotification = (SensorFailNotification) it;
                return new SensorFailNotificationDTO(sensorFailNotification);
            } else {
                return new NotificationDTO(it);
            }
        }).collect(Collectors.toList());
    }
}
