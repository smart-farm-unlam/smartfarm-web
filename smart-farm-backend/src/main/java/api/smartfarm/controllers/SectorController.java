package api.smartfarm.controllers;

import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sector")
@CrossOrigin
public class SectorController {

    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping("/{id}")
    public List<Sector> getById(@PathVariable String id) {
        return sectorService.getSectorsById(id);
    }
}
