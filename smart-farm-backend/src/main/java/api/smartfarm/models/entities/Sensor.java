package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    private String code;
    private SensorTypeId sensorTypeId;
    private SensorStatus status;
    private Measure lastMeasure;
}
