package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.MeasureDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Measure {
    private Date dateTime;
    private Double value;

    //This is temporary until we fix this on the ESP32
    public Measure(MeasureDTO measureDTO) {
        dateTime = (measureDTO.getDateTime() != null)? measureDTO.getDateTime() : new Date();
        value = Math.round(measureDTO.getValue() * 100) / 100.0;
    }
}
