package api.smartfarm.controllers;

import api.smartfarm.models.dtos.SectorDTO;
import api.smartfarm.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<SectorDTO> getSectorById(@PathVariable String farmId) {
        return sectorService.getSectorsById(farmId);
    }
}
