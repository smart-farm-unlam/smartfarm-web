package api.smartfarm.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Farm {
    private String id;
    private String crop;
    private Double earthHumidity;
    private Double earthTemperature;
    private Double ambientHumidity;
}
