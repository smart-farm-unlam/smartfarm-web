package api.smartfarm.controllers;

import api.smartfarm.models.dtos.farms.FarmResponseDTO;
import api.smartfarm.models.dtos.farms.InitFarmRequestDTO;
import api.smartfarm.models.dtos.farms.UpdateFarmRequestDTO;
import api.smartfarm.models.dtos.notifications.NotificationDTO;
import api.smartfarm.models.dtos.weather.ForecastResponseDTO;
import api.smartfarm.models.dtos.weather.WeatherResponseDTO;
import api.smartfarm.services.FarmService;
import api.smartfarm.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/farms")
@CrossOrigin
public class FarmController {

    private final FarmService farmService;
    private final NotificationService notificationService;

    @Autowired
    public FarmController(
        FarmService farmService,
        NotificationService notificationService
    ) {
        this.farmService = farmService;
        this.notificationService = notificationService;
    }

    @GetMapping("/{id}")
    public FarmResponseDTO getById(@PathVariable String id) {
        return farmService.getById(id);
    }

    @PutMapping("/{id}")
    public FarmResponseDTO update(
        @PathVariable String id,
        @RequestBody UpdateFarmRequestDTO updateFarmRequest
    ) {
        return farmService.update(id, updateFarmRequest);
    }

    @PutMapping("/{id}/init")
    @ResponseStatus(HttpStatus.OK)
    public void initFarm(
        @PathVariable String id,
        @Valid @RequestBody InitFarmRequestDTO initFarmRequest
    ) {
        farmService.initFarm(id, initFarmRequest);
    }

    @GetMapping("/{id}/weather")
    public WeatherResponseDTO weather(@PathVariable String id) {
        return farmService.getWeather(id);
    }

    @GetMapping("/{id}/future-weather")
    public List<ForecastResponseDTO> futureWeather(@PathVariable String id) {
        return farmService.futureWeather(id);
    }

    @GetMapping("/{id}/notifications")
    public List<NotificationDTO> getNotifications(@PathVariable String id) {
        return notificationService.getNotifications(id);
    }
}
