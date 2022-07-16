package api.smartfarm.services;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.*;
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

    public List<SectorDTO> getSectors(String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors from farmId: {}", farmId);
        return farm.getSectors().stream().map(SectorDTO::new).collect(Collectors.toList());
    }

    public List<SectorCropTypesDTO> getSectorsCropTypes(String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors + cropType from farmId: {}", farmId);
        return farm.getSectors().stream().map(sector -> {
            String id = sector.getCrop().getType();
            CropType cropType = cropTypeDAO.findById(id).orElseThrow(() -> {
                String errorMsg = "CropType with id " + id + " not exists on database";
                return new NotFoundException(errorMsg);
            });
            return new SectorCropTypesDTO(sector, cropType);
        }).collect(Collectors.toList());
    }

    public SectorDTO create(String farmId, SectorDTO sectorDTO) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Adding sector {} to farm {}", sectorDTO.getCode(), farmId);
        farm.getSectors().add(new Sector(sectorDTO));
        farmService.update(farm);
        return sectorDTO;
    }

    public SectorDTO update(String farmId, String sectorId, SectorDTO sectorDTO) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting farmId: {} for create sectors", farmId);
        farm.getSectors().forEach(sector -> {
            if (sector.getCode().equals(sectorId)) {
                sector.getSensors().addAll(sectorDTO.getSensors());
                LOGGER.info("Update add sensors to farm sector {} {} successfully", farm, sectorId);
            }
        });
        return sectorDTO;
    }

    public void setSectorsCropType(String farmId, List<CropDTO> cropDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        cropDTOS.forEach(cropDTO -> setSectorCropType(farm,cropDTO));
    }

    public void setSectorCropType(Farm farm, CropDTO cropDTO) {
        getCropTypeById(cropDTO.getType()); //Just for validation
        Sector sector = findSectorInFarm(farm, cropDTO.getSectorCode());
        sector.setCrop(new Crop(cropDTO));
        farmService.update(farm);
    }

    public void addSensors(String farmId, List<SensorDTO> sensorDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        sensorDTOS.forEach(sensorDTO -> addSensor(farm,sensorDTO));
    }

    public void addSensor(Farm farm, SensorDTO sensorDTO) {
        Sector sector = findSectorInFarm(farm, sensorDTO.getSectorCode());
        List<Sensor> sensors = sector.getSensors();
        if (sensors == null) {
            sensors = new ArrayList<>();
            sector.setSensors(sensors);
        }
        sensors.add(new Sensor(sensorDTO));
        farmService.update(farm);
    }

    public void addPlants(String farmId, List<PlantDTO> plantDTOS) {
        Farm farm = farmService.getFarmById(farmId);
        plantDTOS.forEach(plantDTO -> addPlant(farm,plantDTO));
    }

    public void addPlant(Farm farm, PlantDTO plantDTO) {
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
        plant.createPlantId(plants.size()+1, sector.getCode());
        plants.add(plant);
        farmService.update(farm);
    }

    private Sector findSectorInFarm(Farm farm, String sectorCode) {
        return farm.getSectors().stream()
                .filter(s -> s.getCode().equalsIgnoreCase(sectorCode))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No sector code: [" + sectorCode + "] " +
                        "in farm id: [" + farm.getId() + "]"));
    }

    private CropType getCropTypeById(String id) {
        return cropTypeDAO.findById(id).orElseThrow(
                () -> new NotFoundException("Crop type: " + id + " not found in database")
        );
    }
}
