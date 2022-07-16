package api.smartfarm.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlantDTO {
    private Integer row;
    private Integer column;
    private String sectorCode;
}
