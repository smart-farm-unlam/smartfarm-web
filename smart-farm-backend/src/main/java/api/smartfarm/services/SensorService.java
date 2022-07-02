package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.SensorDTO;
import api.smartfarm.models.entities.Measure;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.entities.SensorStatus;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.FarmDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Service
public class SensorService {

    private final FarmDAO farmDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);
    private static final Double ERROR_VALUE = -99.0;

    @Autowired
    public SensorService(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public void handleMeasures(String farmId, List<SensorDTO> sensorData) {
        Farm farm = farmDAO.findById(farmId).orElseThrow(() -> {
            String errorMsg = "Farm with id " + farmId + "not exists on database";
            return new NotFoundException(errorMsg);
        });

        List<Sensor> sensors = farm.getSensors();
        List<Sensor> sectorsSensors = farm.getSectors().stream()
            .flatMap(s -> s.getSensors().stream())
            .collect(Collectors.toList());

        for (SensorDTO sd : sensorData) {
            String code = sd.getCode();
            Measure lastMeasure = sd.getMeasures().stream().findFirst().map(Measure::new).orElse(null);
            SensorStatus sensorStatus = resolveSensorStatus(lastMeasure);

            Sensor sensor = sensors.stream()
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(null);

            if (sensor != null) {
                sensor.setStatus(sensorStatus);
                sensor.setLastMeasure(lastMeasure);
            } else {
                sensor = sectorsSensors.stream()
                    .filter(ss -> ss.getCode().equals(code))
                    .findFirst()
                    .orElse(null);

                if (sensor != null) {
                    sensor.setStatus(sensorStatus);
                    sensor.setLastMeasure(lastMeasure);
                } else {
                    sensors.add(new Sensor(code, buildSensorType(code), sensorStatus, lastMeasure));
                }
            }
        }

        farmDAO.save(farm);
        LOGGER.info("Update farm with id {} successfully", farm.getId());
    }

    private String buildSensorType(String code) {
        return SensorTypeId.valueOf(code.substring(0, 2).toUpperCase()).name();
    }

    private SensorStatus resolveSensorStatus(Measure lastMeasure) {
        if (lastMeasure == null || lastMeasure.getValue() == null) return SensorStatus.OFF;
        if (lastMeasure.getValue().equals(ERROR_VALUE)) return SensorStatus.FAIL;
        return SensorStatus.ON;
    }

}
