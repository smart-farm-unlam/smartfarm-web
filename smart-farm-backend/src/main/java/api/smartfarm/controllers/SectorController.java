package api.smartfarm.controllers;

import api.smartfarm.models.dtos.*;
import api.smartfarm.models.dtos.sectors.AddSectorRequestDTO;
import api.smartfarm.models.dtos.sectors.SectorCropTypesDTO;
import api.smartfarm.models.dtos.sectors.SectorResponseDTO;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<SectorResponseDTO> getSectors(@PathVariable String farmId) {
        return sectorService.getSectors(farmId);
    }

    @GetMapping("/{farmId}/crop-types")
    public List<SectorCropTypesDTO> getSectorCropTypes(@PathVariable String farmId) {
        return sectorService.getSectorsCropTypes(farmId, null);
    }

    //This integration could be temporal, is for microcontroller irrigation system
    @GetMapping("/{farmId}/crop-types/micro")
    public List<SectorCropTypesDTO> getSectorCropTypesMicro(@PathVariable String farmId) {
        return sectorService.getSectorsCropTypes(farmId, "microcontroller");
    }

    @PostMapping("/{farmId}")
    @ResponseStatus(HttpStatus.OK)
    public SectorResponseDTO addSector(
        @PathVariable String farmId,
        @Valid @RequestBody AddSectorRequestDTO addSectorRequest
    ) {
        return sectorService.addSector(farmId, addSectorRequest);
    }

    @PostMapping("/{farmId}/set_crops")
    @ResponseStatus(HttpStatus.OK)
    public void updateSectorsCropTypes(
        @PathVariable String farmId,
        @RequestBody List<CropDTO> cropDTOS
    ) {
        sectorService.updateSectorsCropTypes(farmId, cropDTOS);
    }

    @PostMapping("/{farmId}/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void addSensor(
        @PathVariable String farmId,
        @RequestBody List<SensorRequestDTO> sensorRequestDTOS
    ) {
        sectorService.addSensors(farmId, sensorRequestDTOS);
    }

    @PostMapping("/{farmId}/plants")
    @ResponseStatus(HttpStatus.OK)
    public void addPlant(
        @PathVariable String farmId,
        @RequestBody List<PlantDTO> plantDTOS
    ) {
        sectorService.addPlants(farmId, plantDTOS);
    }
}
