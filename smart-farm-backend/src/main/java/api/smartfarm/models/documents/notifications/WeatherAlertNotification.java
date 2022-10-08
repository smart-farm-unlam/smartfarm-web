package api.smartfarm.models.documents.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WeatherAlertNotification extends SmartFarmNotification {

    public WeatherAlertNotification(
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
            NotificationType.WEATHER_ALERT_NOTIFICATION.getDescription(),
            title,
            body
        );
    }
}
