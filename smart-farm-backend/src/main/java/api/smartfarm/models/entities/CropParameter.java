package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CropParameter {
    private String name;
    private Double min;
    private Double max;
    private String unit;
    private String relatedSensor;
}
