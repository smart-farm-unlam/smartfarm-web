package api.smartfarm.models.dtos.sensors;

import api.smartfarm.models.dtos.MeasureDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorRequestDTO {
    private String code;
    private String sectorCode;
    private List<MeasureDTO> measures;
}