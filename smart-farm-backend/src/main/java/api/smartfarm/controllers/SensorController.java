package api.smartfarm.controllers;

import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
@CrossOrigin
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{farmId}/events")
    @ResponseStatus(HttpStatus.OK)
    public void sensorData(
        @PathVariable String farmId,
        @RequestBody List<SensorRequestDTO> sensorsData
    ) {
        sensorService.handleMeasures(farmId, sensorsData);
    }
}
