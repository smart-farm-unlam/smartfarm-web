package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

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

    @Transient
    private static final Double ERROR_VALUE = -99.0;

    public Sensor(SensorRequestDTO sensorRequestDTO) {
        this.code = sensorRequestDTO.getCode();
        this.sensorTypeId = resolveSensorTypeId();
        this.lastMeasure = (sensorRequestDTO.getMeasure() != null) ?
            new Measure(sensorRequestDTO.getMeasure()) : null;
        this.status = resolveSensorStatus();
    }

    private SensorTypeId resolveSensorTypeId() {
        return SensorTypeId.valueOf(code.substring(0, 2).toUpperCase());
    }

    public SensorStatus resolveSensorStatus() {
        SensorStatus statusResponse = SensorStatus.OFF;
        if (lastMeasure != null && lastMeasure.getValue() != null) {
            statusResponse = (lastMeasure.getValue().equals(ERROR_VALUE)) ? SensorStatus.FAIL : SensorStatus.ON;
        }
        return statusResponse;
    }
}
