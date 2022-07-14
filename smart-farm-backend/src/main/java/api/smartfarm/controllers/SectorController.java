package api.smartfarm.controllers;

import api.smartfarm.models.dtos.SectorCropTypesDTO;
import api.smartfarm.models.dtos.SectorDTO;
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
}
