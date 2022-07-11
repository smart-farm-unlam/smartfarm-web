package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {
    private String code;
    private SensorTypeId sensorTypeId;
    private SensorStatus status;
    private Measure lastMeasure;
}
