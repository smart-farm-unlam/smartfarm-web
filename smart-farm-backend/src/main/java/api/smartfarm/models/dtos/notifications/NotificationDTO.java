package api.smartfarm.models.dtos.notifications;

import api.smartfarm.models.documents.notifications.SmartFarmNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDTO {
    private Date date;
    private String farmId;
    private String userId;
    private String type;
    private String title;
    private String body;

    public NotificationDTO(SmartFarmNotification smartFarmNotification) {
        this.date = smartFarmNotification.getDate();
        this.farmId = smartFarmNotification.getFarmId();
        this.userId = smartFarmNotification.getUserId();
        this.type = smartFarmNotification.getType();
        this.title = smartFarmNotification.getTitle();
        this.body = smartFarmNotification.getBody();
    }
}
