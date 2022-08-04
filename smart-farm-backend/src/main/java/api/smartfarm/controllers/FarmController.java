package api.smartfarm.controllers;

import api.smartfarm.models.dtos.WeatherResponseDTO;
import api.smartfarm.models.dtos.farms.CreateFarmRequestDTO;
import api.smartfarm.models.dtos.farms.FarmResponseDTO;
import api.smartfarm.models.dtos.farms.InitFarmRequestDTO;
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
    public FarmResponseDTO create(@Valid @RequestBody CreateFarmRequestDTO farm) {
        return farmService.create(farm);
    }

    @GetMapping("/{id}")
    public FarmResponseDTO getById(@PathVariable String id) {
        return farmService.getById(id);
    }

    @PutMapping("/{id}/init")
    @ResponseStatus(HttpStatus.OK)
    public void initFarm(
        @PathVariable String id,
        @Valid @RequestBody InitFarmRequestDTO initRequest
    ) {
        farmService.initFarm(id, initRequest);
    }

    @GetMapping("/{id}/weather")
    public WeatherResponseDTO weather(@PathVariable String id) {
        return farmService.getWeather(id);
    }

}
