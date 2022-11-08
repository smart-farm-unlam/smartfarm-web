package api.smartfarm.controllers;

import api.smartfarm.models.documents.MockedValue;
import api.smartfarm.models.dtos.AverageMeasureHistoricDTO;
import api.smartfarm.models.dtos.sensors.MockedValueRequestDTO;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.models.entities.SensorDateFilter;
import api.smartfarm.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/{farmId}")
    @ResponseStatus(HttpStatus.OK)
    public void sensorsData(
        @PathVariable String farmId,
        @RequestBody List<SensorRequestDTO> sensorsData
    ) {
        sensorService.handleMeasures(farmId, sensorsData);
    }

    @GetMapping("/{farmId}/{sensorCode}/historic")
    public List<AverageMeasureHistoricDTO> getAverageHistoric(
        @PathVariable String farmId,
        @PathVariable String sensorCode,
        @RequestParam SensorDateFilter dateFilter
    ) {
        return sensorService.getAverageHistoric(farmId, sensorCode, dateFilter);
    }

    //This is for anti-frost system demo, not for final product
    @GetMapping("/{sensorCode}/mock")
    public MockedValue mockedValueBySensorCode(
        @PathVariable String sensorCode
    ) {
        return sensorService.getMockValueBySensorCode(sensorCode);
    }

    //This is for anti-frost system demo, not for final product
    @PutMapping("/{sensorCode}/mock")
    public MockedValue setMockedValueBySensorCode(
        @PathVariable String sensorCode,
        @Valid @RequestBody MockedValueRequestDTO mockedValueRequestDTO
    ) {
        return sensorService.setMockedValueBySensorCode(sensorCode, mockedValueRequestDTO);
    }
}
