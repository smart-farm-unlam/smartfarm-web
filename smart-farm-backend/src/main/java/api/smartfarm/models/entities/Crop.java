package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.CropDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Crop {
    private String type;
    private List<Plant> plants;

    public Crop(CropDTO cropDTO) {
        this.type = cropDTO.getType();
        this.plants = new ArrayList<>();
        createPlants(cropDTO.getPlantsCount(), cropDTO.getSectorCode());
    }

    private void createPlants(Integer plantsNbr, String sectorCode) {
        for (int i = 0; i < plantsNbr; i++) {
            Plant plant = new Plant();
            plant.createPlantId((i + 1), sectorCode);
            this.plants.add(plant);
        }
    }
}
