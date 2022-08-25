package api.smartfarm.models.dtos.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public abstract class NotificationDTO {
    private Date date;
    private String farmId;
    private String userId;
}
