package api.smartfarm.models.dtos.notifications;

import api.smartfarm.models.documents.notifications.SensorFailNotification;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SensorFailNotificationDTO extends NotificationDTO {

    private String sensorCode;

    public SensorFailNotificationDTO(SensorFailNotification sensorFailNotification) {
        super(
            sensorFailNotification.getDate(),
            sensorFailNotification.getFarmId(),
            sensorFailNotification.getUserId(),
            sensorFailNotification.getType(),
            sensorFailNotification.getTitle(),
            sensorFailNotification.getBody()
        );
        this.sensorCode = sensorFailNotification.getSensorCode();
    }
}
