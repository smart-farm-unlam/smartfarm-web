package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {
    private String id;
    private SensorStatus status;
    private List<Measure> measures;
}
