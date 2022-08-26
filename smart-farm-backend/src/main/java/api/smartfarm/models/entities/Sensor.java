package api.smartfarm.models.entities;

import api.smartfarm.models.documents.Measure;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sensor {

    private String code;
    private SensorTypeId sensorTypeId;
    private SensorStatus status;

    @JsonProperty("lastMeasure")
    @DocumentReference
    private Measure lastMeasure;

    @Transient
    private static final Double ERROR_VALUE = -99.0;

    public Sensor(SensorRequestDTO sensorRequestDTO, Measure lastMeasure) {
        this.code = sensorRequestDTO.getCode();
        this.sensorTypeId = resolveSensorTypeId();
        this.lastMeasure = lastMeasure;
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
