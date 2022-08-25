package api.smartfarm.models.documents;

import api.smartfarm.models.dtos.MeasureDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "measures")
public class Measure {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String farmId;
    @JsonIgnore
    private String sensorCode;

    private Date dateTime;
    private Double value;

    //If dateTime from ESP32 is null set new Date()
    public Measure(String farmId, String sensorCode, MeasureDTO measureDTO) {
        this.farmId = farmId;
        this.sensorCode = sensorCode;
        dateTime = (measureDTO.getDateTime() != null)? measureDTO.getDateTime() : new Date();
        value = Math.round(measureDTO.getValue() * 100) / 100.0;
    }
}
