package api.smartfarm.models.documents.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SensorFailNotification extends SmartFarmNotification {
    private String sensorCode;

    public SensorFailNotification(
        Date date,
        String farmId,
        String userId,
        List<String> devices,
        String sensorCode
    ) {
        super(date, farmId, userId, devices);
        this.sensorCode = sensorCode;
    }
}