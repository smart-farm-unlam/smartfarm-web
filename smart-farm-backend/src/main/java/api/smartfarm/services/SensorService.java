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

import static api.smartfarm.models.documents.SensorType.SensorTypeId;

@Service
public class SensorService {

    private final FarmDAO farmDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    public SensorService(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public void handleMeasures(String farmId, List<SensorDTO> sensorData) {
        Farm farm = farmDAO.findById(farmId).orElseThrow(() -> {
            LOGGER.error("Farm with id {} not exists on database", farmId);
            return new NotFoundException("Farm not exists on database");
        });

        List<Sensor> farmSensors = farm.getSensors();

        for (SensorDTO sd : sensorData) {
            String code = sd.getCode();
            Measure lastMeasure = sd.getMeasures().stream().findFirst().map(Measure::new).orElse(null);

            Sensor sensor = farmSensors.stream()
                .filter(s -> s.getCode().equals(code))
                .findAny()
                .orElse(null);

            if (sensor != null) {
                sensor.setStatus(SensorStatus.ON);
                sensor.setLastMeasure(lastMeasure);
            } else {
                farmSensors.add(new Sensor(code, buildSensorType(code), SensorStatus.ON, lastMeasure));
            }
        }

        farmDAO.save(farm);
        LOGGER.info("Update farm with id {} successfully", farm.getId());
    }

    private String buildSensorType(String code) {
        return SensorTypeId.valueOf(code.substring(0, 2).toUpperCase()).name();
    }

}
