package api.smartfarm.schedules;

import api.smartfarm.config.DynamicConfigurationProperties;
import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.Measure;
import api.smartfarm.models.documents.notifications.NotificationStatus;
import api.smartfarm.models.documents.notifications.ParameterOutOfRangeNotification;
import api.smartfarm.models.entities.AverageMeasure;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.CropTypeDAO;
import api.smartfarm.repositories.FarmDAO;
import api.smartfarm.repositories.MeasureDAO;
import api.smartfarm.repositories.NotificationDAO;
import api.smartfarm.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class MeasuresControlSchedule {

    private final FarmDAO farmDAO;

    private final CropTypeDAO cropTypeDAO;

    private final MeasureDAO measureDAO;

    private final NotificationDAO notificationDAO;

    NotificationService notificationService;

    @Autowired
    public MeasuresControlSchedule(
            FarmDAO farmDAO,
            CropTypeDAO cropTypeDAO,
            MeasureDAO measureDAO,
            NotificationDAO notificationDAO,
            NotificationService notificationService) {
        this.farmDAO = farmDAO;
        this.cropTypeDAO = cropTypeDAO;
        this.measureDAO = measureDAO;
        this.notificationDAO = notificationDAO;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "#{@cronInterval}")
    void checkFarmsMeasuresSchedule() {
        List<Farm> farms = farmDAO.findAll();
        farms.forEach(this::checkFarmMeasures);
    }

    private void checkFarmMeasures(Farm farm) {
        List<String> farmNotOkValues = getFarmNotOkValues(farm);

        if (!farmNotOkValues.isEmpty() && !notificationSentRecently(farm.getId()))
            notificationService.sendParameterOutOfRange(String.join("\n",farmNotOkValues),farm);
    }

    private List<String> getFarmNotOkValues(Farm farm) {
        List<String> farmNotOkValues = new ArrayList<>();
        List<AverageMeasure> averageMeasures = new ArrayList<>();
        List<CropType> cropTypes = new ArrayList<>();

        loadSensorsAverageMeasures(farm.getId(), farm.getSensors(), averageMeasures);

        List<Sector> farmSectors = farm.getSectors();
        farmSectors.forEach(sector -> {
            loadSensorsAverageMeasures(farm.getId(), sector.getSensors(), averageMeasures);

            CropType cropType = cropTypeDAO.findById(sector.getCrop().getType()).orElseThrow(() -> {
                String errorMsg = "No crop type " + sector.getCrop().getType() + " was found on database";
                return new NotFoundException(errorMsg);
            });
            cropTypes.add(cropType);
        });

        averageMeasures.forEach(averageMeasure -> {
            cropTypes.forEach(cropType -> {
                AverageMeasure.AverageMeasureLevel measureLevel = cropType.checkAverageMeasure(averageMeasure);
                if(measureLevel==AverageMeasure.AverageMeasureLevel.BELOW_MIN){
                    averageMeasure.addBelowMinFor(cropType.getName());
                } else if(measureLevel==AverageMeasure.AverageMeasureLevel.OVER_MAX){
                    averageMeasure.addOverMaxFor(cropType.getName());
                }
            });
            String msg = averageMeasure.getNotificationMsg();
            if(msg!=null)
                farmNotOkValues.add(msg);
        });

        return farmNotOkValues;
    }

    private boolean notificationSentRecently(String farmId) {
        List<ParameterOutOfRangeNotification> notifications = notificationDAO
                .findByFarmIdAndClassAndStatusOrderByDate(
                        farmId,
                        ParameterOutOfRangeNotification.class.getName(),
                        NotificationStatus.SENT.name()
                );
        ParameterOutOfRangeNotification notification = notifications.get(0);
        if(notification==null)
            return false;
        Date currentDate = new Date();
        long timeToResend = DynamicConfigurationProperties.getDynamicConfiguration().getTimeToReSendNotification();
        return (currentDate.getTime() - notification.getDate().getTime()) < timeToResend;
    }

    private void loadSensorsAverageMeasures(String farmId, List<Sensor> sensors, List<AverageMeasure> averageMeasures) {
        sensors.forEach(sensor -> {
            AverageMeasure averageMeasure = getSensorAvgMeasure(farmId, sensor);
            if (averageMeasure != null)
                averageMeasures.add(averageMeasure);
        });
    }

    private AverageMeasure getSensorAvgMeasure(String farmId, Sensor sensor) {
        List<Measure> measures = measureDAO.findTop5ByFarmIdAndSensorCodeOrderByDateTime(farmId, sensor.getCode());
        if (measures!=null && !measures.isEmpty()) {
            float average = 0;
            int validMeasureCount = 0;
            for (Measure measure : measures) {
                if (!Objects.equals(measure.getValue(), Sensor.ERROR_VALUE)) {
                    average += measure.getValue();
                    validMeasureCount++;
                }
            }
            if (validMeasureCount > 0)
                return new AverageMeasure((double) (average / validMeasureCount),
                        sensor.getSensorTypeId());
        }
        return null;
    }
}