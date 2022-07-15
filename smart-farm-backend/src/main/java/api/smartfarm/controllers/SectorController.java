package api.smartfarm.controllers;

import api.smartfarm.models.dtos.*;
import api.smartfarm.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sectors")
@CrossOrigin
public class SectorController {

    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping("/{farmId}")
    public List<SectorDTO> getSectors(@PathVariable String farmId) {
        return sectorService.getSectors(farmId);
    }

    //This integration could be temporal, is for microcontroller irrigation system
    @GetMapping("/{farmId}/crop-types")
    public List<SectorCropTypesDTO> getSectorCropTypes(@PathVariable String farmId) {
        return sectorService.getSectorsCropTypes(farmId);
    }

    @PostMapping("/{farmId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SectorDTO create(@PathVariable String farmId, @RequestBody SectorDTO sectorDTO) {
        return sectorService.create(farmId, sectorDTO);
    }

    @PutMapping("/{farmId}/{sectorId}")
    @ResponseStatus(HttpStatus.OK)
    public SectorDTO update(@PathVariable String farmId, @PathVariable String sectorId, @RequestBody SectorDTO sectorDTO) {
        return sectorService.update(farmId, sectorId, sectorDTO);
    }

    @PostMapping("/{farmId}/set_crops")
    @ResponseStatus(HttpStatus.CREATED)
    public void setSectorsCropType(
            @PathVariable String farmId,
            @RequestBody List<CropDTO> cropDTOS) {
        sectorService.setSectorsCropType(farmId,cropDTOS);
    }

    @PostMapping("/{farmId}/sensors")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSensor(
            @PathVariable String farmId,
            @RequestBody List<SensorDTO> sensorDTOS
    ) {
        sectorService.addSensors(farmId, sensorDTOS);
    }

    @PostMapping("/{farmId}/plants")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlant(
            @PathVariable String farmId,
            @RequestBody List<PlantDTO> plantDTOS
    ) {
        sectorService.addPlants(farmId, plantDTOS);
    }
}
