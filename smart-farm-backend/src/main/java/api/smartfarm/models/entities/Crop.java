package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.CropDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Crop {
    private String type;
    private List<Plant> plants;

    public Crop(String cropType) {
        this.type = cropType;
        this.plants = new ArrayList<>();
    }

    public Crop(CropDTO cropDTO) {
        this.type = cropDTO.getType();
        this.plants = new ArrayList<>();
        createPlants(cropDTO.getPlantsCount(), cropDTO.getSectorCode());
    }

    private void createPlants(int plantsCount, String sectorCode) {
        for (int i = 0; i < plantsCount; i++) {
            Plant plant = new Plant();
            plant.createPlantId((i + 1), sectorCode);
            this.plants.add(plant);
        }
    }
}
