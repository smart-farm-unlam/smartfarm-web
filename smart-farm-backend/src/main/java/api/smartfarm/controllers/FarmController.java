package api.smartfarm.controllers;

import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.services.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/farms")
@CrossOrigin
public class FarmController {

    private final FarmService farmService;

    @Autowired
    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FarmDTO create(@Valid @RequestBody FarmDTO farm) {
        return farmService.create(farm);
    }

    @GetMapping("/{id}")
    public FarmDTO getById(@PathVariable String id) {
        return farmService.getById(id);
    }

}
