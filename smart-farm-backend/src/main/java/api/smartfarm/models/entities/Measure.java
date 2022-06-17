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

    public Measure(MeasureDTO measureDTO) {
        dateTime = measureDTO.getDateTime();
        value = measureDTO.getValue();
    }
}
