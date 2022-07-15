package api.smartfarm.models.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorDTO {
    private String code;
    private String sectorCode;
    private List<MeasureDTO> measures;
}