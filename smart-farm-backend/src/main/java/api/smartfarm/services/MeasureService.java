package api.smartfarm.services;

import api.smartfarm.models.documents.Measure;
import api.smartfarm.models.dtos.AverageMeasureHistoricDTO;
import api.smartfarm.models.entities.AverageMeasure;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.entities.SensorDateFilter;
import api.smartfarm.models.exceptions.DateParseException;
import api.smartfarm.repositories.MeasureDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static api.smartfarm.models.entities.SensorDateFilter.DAY;

@Service
public class MeasureService {

    private final MeasureDAO measureDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureService.class);

    @Autowired
    public MeasureService(MeasureDAO measureDAO) {
        this.measureDAO = measureDAO;
    }

    public void saveMeasure(Measure measure) {
        measureDAO.save(measure);
        LOGGER.info("Measure created with id {}", measure.getId());
    }

    public List<AverageMeasureHistoricDTO> getAverageMeasureBySensorCode(
        String farmId,
        String sensorCode,
        SensorDateFilter sensorDateFilter
    ) {
        Date dateFrom = new Date();
        ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        switch (sensorDateFilter) {
            case DAY:
                dateFrom = Date.from(today.toInstant());
                break;
            case WEEK:
                dateFrom = Date.from(today.minusWeeks(1).toInstant());
                break;
            case MONTH:
                dateFrom = Date.from(today.minusMonths(1).toInstant());
                break;
            case THREE_MONTHS:
                dateFrom = Date.from(today.minusMonths(3).toInstant());
                break;
        }

        List<Measure> measures = measureDAO.findByFarmIdAndSensorCodeAndDateTimeGreaterThanEqualOrderByDateTime(
            farmId,
            sensorCode,
            dateFrom
        );
        if (sensorDateFilter == DAY) {
            return measures.stream()
                .map(AverageMeasureHistoricDTO::new)
                .collect(Collectors.toList());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        Map<Date, List<Measure>> measuresByDate = measures.stream()
            .collect(
                Collectors.groupingBy(m -> {
                        try {
                            return formatter.parse(formatter.format(m.getDateTime()));
                        } catch (ParseException e) {
                            throw new DateParseException("Error parsing sensor date filter");
                        }
                    },
                    LinkedHashMap::new,
                    Collectors.toList()
                )
            );

        List<AverageMeasureHistoricDTO> averageMeasureHistoricDTOs = new ArrayList<>();
        measuresByDate.forEach((date, value) -> {
            Double averageValue = value.stream().mapToDouble(Measure::getValue).average().orElse(-99);
            averageMeasureHistoricDTOs.add(new AverageMeasureHistoricDTO(date, sensorCode, averageValue));
        });

        return averageMeasureHistoricDTOs;
    }

    public List<AverageMeasure> getAverageMeasures(String farmId, List<Sensor> sensors) {
        List<AverageMeasure> averageMeasures = new ArrayList<>();
        sensors.forEach(sensor -> {
            AverageMeasure averageMeasure = getSensorAvgMeasure(farmId, sensor);
            if (averageMeasure != null)
                averageMeasures.add(averageMeasure);
        });

        return averageMeasures;
    }

    private AverageMeasure getSensorAvgMeasure(String farmId, Sensor sensor) {
        List<Measure> measures = measureDAO.findTop5ByFarmIdAndSensorCodeOrderByDateTimeDesc(farmId, sensor.getCode());
        if (!measures.isEmpty()) {
            Double totalValue = 0.0;
            int validMeasureCount = 0;
            for (Measure measure : measures) {
                if (!Sensor.ERROR_VALUE.equals(measure.getValue())) {
                    totalValue += measure.getValue();
                    validMeasureCount++;
                }
            }
            if (validMeasureCount > 0) {
                Double average = totalValue / validMeasureCount;
                return new AverageMeasure(average, sensor.getSensorTypeId());
            }
        }
        return null;
    }

    public Measure getLastMeasureByFarmId(String farmId) {
        return measureDAO.findTop1ByFarmIdOrderByDateTimeDesc(farmId);
    }
}
