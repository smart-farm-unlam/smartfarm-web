package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.PlantDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plant {

    private String id;
    private Integer row;
    private Integer column;

    public Plant(PlantDTO plantDTO) {
        this.row = plantDTO.getRow() == null ? 0 : plantDTO.getRow();
        this.column = plantDTO.getColumn() == null ? 0 : plantDTO.getColumn();
    }

    public void createPlantId(int idx, String sectorCode) {
        this.id = "P" + idx + sectorCode;
    }
}
