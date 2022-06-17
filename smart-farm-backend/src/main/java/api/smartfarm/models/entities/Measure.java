package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Measure {
    private Date dateTime;
    private Double value;
}
