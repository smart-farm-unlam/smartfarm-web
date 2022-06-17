package api.smartfarm.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MeasureDTO {
    private Date dateTime;
    private Double value;
}
