package api.smartfarm.models.documents.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FarmDisconnectedNotification extends SmartFarmNotification {

    private Date lastMeasureDateTime;

    public FarmDisconnectedNotification(
            Date date,
            String farmId,
            String userId,
            List<String> deviceIds,
            String title,
            String body,
            Date lastMeasureDateTime
    ) {
        super(
                date,
                farmId,
                userId,
                deviceIds,
                NotificationType.FARM_DISCONNECTED_NOTIFICATION.getDescription(),
                title,
                body
        );
        this.lastMeasureDateTime = lastMeasureDateTime;
    }
}
