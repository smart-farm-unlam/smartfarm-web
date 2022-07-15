package api.smartfarm.services;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.PlantDTO;
import api.smartfarm.models.dtos.SectorCropTypesDTO;
import api.smartfarm.models.dtos.SectorDTO;
import api.smartfarm.models.dtos.SensorDTO;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);
    private final FarmService farmService;
    private final CropTypeDAO cropTypeDAO;

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

    public void addSensor(String farmId, SensorDTO sensorDTO) {
        Farm farm = farmService.getFarmById(farmId);
        Sector sector = findSectorInFarm(farm, sensorDTO.getSectorCode());
        List<Sensor> sensors = sector.getSensors();
        if (sensors == null) {
            sensors = new ArrayList<>();
        }
        sensors.add(new Sensor(sensorDTO));
        farmService.update(farm);
    }

    private Sector findSectorInFarm(Farm farm, String sectorCode) {
        return farm.getSectors().stream()
                .filter(s -> s.getCode().equalsIgnoreCase(sectorCode))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No sector code: [" + sectorCode + "] " +
                        "in farm id: [" + farm.getId() + "]"));
    }

    public void addPlant(String farmId, PlantDTO plantDTO) {
        Farm farm = farmService.getFarmById(farmId);
        Sector sector = findSectorInFarm(farm, plantDTO.getSectorCode());
        if (sector.getCrop() == null) {
            throw new NotFoundException("No crop assigned to sector: [" + sector.getCode() + "] on farm: [" + farmId + "]");
        }
        List<Plant> plants = sector.getCrop().getPlants();
        if (plants == null) {
            plants = new ArrayList<>();
        }
        plants.add(new Plant(plantDTO));
        farmService.update(farm);
    }
}
