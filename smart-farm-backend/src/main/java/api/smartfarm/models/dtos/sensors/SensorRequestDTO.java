package api.smartfarm.models.dtos.sensors;

import api.smartfarm.models.dtos.MeasureDTO;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorRequestDTO {
    @NotEmpty
    private String code;
    private String sectorCode;
    private MeasureDTO measure;
}