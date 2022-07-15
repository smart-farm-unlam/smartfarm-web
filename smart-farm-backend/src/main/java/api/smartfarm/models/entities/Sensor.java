package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.SensorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {

    @Transient
    private static final Double ERROR_VALUE = -99.0;
    private String code;
    private SensorTypeId sensorTypeId;
    private SensorStatus status;
    private Measure lastMeasure;

    public Sensor(SensorDTO sensorDTO) {
        buildSensorType(sensorDTO.getCode());
        this.lastMeasure = sensorDTO.getMeasures().stream().findFirst().map(Measure::new).orElse(null);
        this.resolveSensorStatus();
    }

    private void buildSensorType(String code) {
        this.sensorTypeId = SensorTypeId.valueOf(code.substring(0, 2).toUpperCase());
    }

    public void resolveSensorStatus() {
        this.status = SensorStatus.OFF;
        if (lastMeasure != null && lastMeasure.getValue() != null) {
            this.status = (lastMeasure.getValue().equals(ERROR_VALUE))
                    ? SensorStatus.FAIL : SensorStatus.ON;
        }
    }
}
