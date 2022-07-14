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
    private List<MeasureDTO> measures;
}