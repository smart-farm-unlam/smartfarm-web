package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {
    private String code;
    private String sensorTypeId;
    private SensorStatus status;
    private Measure lastMeasure;
}
