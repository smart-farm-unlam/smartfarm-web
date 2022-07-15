package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.CropDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Crop {
    private String type;
    private List<Plant> plants;

    public Crop(CropDTO cropDTO) {
        this.type = cropDTO.getType();
    }
}
