package api.smartfarm.models.documents.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParameterOutOfRangeNotification extends SmartFarmNotification {

    public ParameterOutOfRangeNotification(
        Date date,
        String farmId,
        String userId,
        List<String> devices,
        String title,
        String body
    ) {
        super(
            date,
            farmId,
            userId,
            devices,
            NotificationType.PARAMETER_OUT_OF_RANGE.getDescription(),
            title,
            body
        );
    }
}