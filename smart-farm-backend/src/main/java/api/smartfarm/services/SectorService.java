package api.smartfarm.services;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.CropDTO;
import api.smartfarm.models.dtos.PlantDTO;
import api.smartfarm.models.dtos.sectors.AddSectorRequestDTO;
import api.smartfarm.models.dtos.sectors.SectorCropTypesDTO;
import api.smartfarm.models.dtos.sectors.SectorResponseDTO;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.models.entities.Crop;
import api.smartfarm.models.entities.Plant;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.CropTypeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorService {

    private final FarmService farmService;
    private final CropTypeDAO cropTypeDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);

    @Autowired
    public SectorService(FarmService farmService, CropTypeDAO cropTypeDAO) {
        this.farmService = farmService;
        this.cropTypeDAO = cropTypeDAO;
    }

    public List<SectorResponseDTO> getSectors(String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors from farmId: {}", farmId);
        return farm.getSectors().stream().map(SectorResponseDTO::new).collect(Collectors.toList());
    }

    public List<SectorCropTypesDTO> getSectorsCropTypes(
        String farmId,
        String source
    ) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors + cropType from farmId: {}", farmId);
        return farm.getSectors().stream().map(sector -> {
            String id = sector.getCrop().getType();
            CropType cropType = cropTypeDAO.findById(id).orElseThrow(() -> {
                String errorMsg = "CropType with id " + id + " not exists on database";
                return new NotFoundException(errorMsg);
            });
            if ("microcontroller".equals(source)){
                cropType.setOptimalEnvironment(null);
                cropType.setPlantation(null);
                cropType.setProperties(null);
                cropType.setHarvest(null);
                cropType.setFaq(null);
            }
            return new SectorCropTypesDTO(sector, cropType);
        }).collect(Collectors.toList());
    }

    public SectorResponseDTO addSector(String farmId, AddSectorRequestDTO sectorRequestDTO) {
        CropType cropType = getCropType(sectorRequestDTO.getCropType());

        Farm farm = farmService.getFarmById(farmId);
        List<Sector> sectors = farm.getSectors();
        String sectorCode = "S";

        int row = 0;
        int column = 0;
        if (sectors == null) {
            sectors = new ArrayList<>();
            sectorCode = sectorCode.concat("1");
        } else {
            sectorCode += String.valueOf(sectors.size() + 1);
            column = sectors.size();
        }

        Sector newSector = new Sector(sectorCode, cropType.getId(), row, column);

        for (int i = 0; i < sectorRequestDTO.getPlantsCount(); i++) {
            Plant plant = new Plant();
            plant.createPlantId((i + 1), sectorCode);
            newSector.getCrop().getPlants().add(plant);
        }

        sectors.add(newSector);
        farmService.update(farm);
        return new SectorResponseDTO(newSector);
    }


    public void updateSectorsCropTypes(String farmId, List<CropDTO> cropDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        cropDTOS.forEach(cropDTO -> setSectorCropType(farm, cropDTO));
    }

    public void setSectorCropType(Farm farm, CropDTO cropDTO) {
        getCropType(cropDTO.getType());
        Sector sector = findSectorInFarm(farm, cropDTO.getSectorCode());
        sector.setCrop(new Crop(cropDTO));
        farmService.update(farm);
    }

    public void addSensors(String farmId, List<SensorRequestDTO> sensorRequestDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        sensorRequestDTOS.forEach(sensorDTO -> addSensor(farm, sensorDTO));
    }

    private void addSensor(Farm farm, SensorRequestDTO sensorRequestDTO) {
        Sector sector = findSectorInFarm(farm, sensorRequestDTO.getSectorCode());
        List<Sensor> sensors = sector.getSensors();
        if (sensors == null) {
            sensors = new ArrayList<>();
        }

        Sensor sensor = farm.getSensors().stream()
            .filter(s -> s.getCode().equalsIgnoreCase(sensorRequestDTO.getCode()))
            .findFirst()
            .orElse(null);

        if (sensor != null) {
            sensors.add(sensor);
            farm.getSensors().remove(sensor);
        } else {
            sensors.add(new Sensor(sensorRequestDTO));
        }
        farmService.update(farm);
    }

    public void addPlants(String farmId, List<PlantDTO> plantDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        plantDTOS.forEach(plantDTO -> addPlant(farm, plantDTO));
    }

    private void addPlant(Farm farm, PlantDTO plantDTO) {
        Sector sector = findSectorInFarm(farm, plantDTO.getSectorCode());
        if (sector.getCrop() == null) {
            throw new NotFoundException("No crop assigned to sector: [" + sector.getCode() + "] on farm: [" + farm.getId() + "]");
        }
        List<Plant> plants = sector.getCrop().getPlants();
        if (plants == null) {
            plants = new ArrayList<>();
            sector.getCrop().setPlants(plants);
        }
        Plant plant = new Plant(plantDTO);
        plant.createPlantId(plants.size() + 1, sector.getCode());
        plants.add(plant);
        farmService.update(farm);
    }

    private Sector findSectorInFarm(Farm farm, String sectorCode) {
        return farm.getSectors().stream()
            .filter(s -> s.getCode().equalsIgnoreCase(sectorCode))
            .findFirst()
            .orElseThrow(
                () -> new NotFoundException("No sector code: " + sectorCode + " in farm id: " + farm.getId())
            );
    }

    private CropType getCropType(String id) {
        return cropTypeDAO.findById(id).orElseThrow(() -> new NotFoundException("Crop type: " + id + " not found in database"));
    }
}
