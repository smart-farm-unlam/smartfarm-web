package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.PlantDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Plant {

    private String id;
    private Integer row;
    private Integer column;
    private List<String> diagnostics;

    public Plant(PlantDTO plantDTO) {
        this.row = plantDTO.getRow();
        this.column = plantDTO.getColumn();
    }
}
