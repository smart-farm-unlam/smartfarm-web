package api.smartfarm.models.dtos.sensors;

import api.smartfarm.models.dtos.MeasureDTO;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorRequestDTO {
    @NotEmpty
    private String code;
    private String sectorCode;
    private List<MeasureDTO> measures;
}