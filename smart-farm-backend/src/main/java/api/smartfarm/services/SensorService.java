package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.Measure;
import api.smartfarm.models.dtos.AverageMeasureHistoricDTO;
import api.smartfarm.models.dtos.sensors.SensorRequestDTO;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.entities.SensorDateFilter;
import api.smartfarm.models.entities.SensorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorService {

    private final FarmService farmService;
    private final MeasureService measureService;
    private final NotificationService notificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    public SensorService(FarmService farmService, MeasureService measureService1, NotificationService notificationService) {
        this.farmService = farmService;
        this.measureService = measureService1;
        this.notificationService = notificationService;
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
            Measure lastMeasure = new Measure(farmId, code, sd.getMeasure());

            Sensor sensor = sensors.stream()
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(null);

            if (sensor != null) {
                sensor.setLastMeasure(lastMeasure);
                sensor.setStatus(sensor.resolveSensorStatus());
            } else {
                sensor = sectorsSensors.stream()
                    .filter(ss -> ss.getCode().equals(code))
                    .findFirst()
                    .orElse(null);

                if (sensor != null) {
                    sensor.setLastMeasure(lastMeasure);
                    sensor.setStatus(sensor.resolveSensorStatus());
                } else {
                    sensor = new Sensor(sd, lastMeasure);
                    sensors.add(sensor);
                }
            }

            measureService.saveMeasure(lastMeasure);

            if (SensorStatus.FAIL == sensor.getStatus()) {
                notificationService.sendSensorFailNotification(sensor.getCode(), farm);
            } else if (SensorStatus.OUT_RAGE == sensor.getStatus()) {
                notificationService.sendParameterOutOfRange(sensor.getCode(), farm);
            }
        }

        farmService.update(farm);
        LOGGER.info("Update farm with id {} successfully", farm.getId());
    }

    public List<AverageMeasureHistoricDTO> getAverageHistoric(String farmId, String sensorCode, SensorDateFilter sensorDateFilter) {
        return measureService.getAverageMeasureBySensorCode(farmId, sensorCode, sensorDateFilter);
    }
}
