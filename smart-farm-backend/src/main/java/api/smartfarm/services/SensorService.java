package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.models.entities.Measure;
import api.smartfarm.models.entities.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorService {

    private final FarmService farmService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    public SensorService(FarmService farmService) {
        this.farmService = farmService;
    }

    public void handleMeasures(String farmId, List<SensorRequestDTO> sensorData) {
        Farm farm = farmService.getFarmById(farmId);

        LOGGER.info("Sensors Data received: {}", sensorData);
        List<Sensor> sensors = farm.getSensors();
        List<Sensor> sectorsSensors = farm.getSectors().stream()
            .flatMap(s -> s.getSensors().stream())
            .collect(Collectors.toList());

        for (SensorRequestDTO sd : sensorData) {
            String code = sd.getCode();
            Measure lastMeasure = sd.getMeasures().stream().findFirst().map(Measure::new).orElse(null);

            Sensor sensor = sensors.stream()
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(null);

            if (sensor != null) {
                sensor.setLastMeasure(lastMeasure);
                sensor.resolveSensorStatus();
            } else {
                sensor = sectorsSensors.stream()
                    .filter(ss -> ss.getCode().equals(code))
                    .findFirst()
                    .orElse(null);

                if (sensor != null) {
                    sensor.setLastMeasure(lastMeasure);
                    sensor.resolveSensorStatus();
                } else {
                    sensors.add(new Sensor(sd));
                }
            }
        }

        farmService.update(farm);
        LOGGER.info("Update farm with id {} successfully", farm.getId());
    }


}
