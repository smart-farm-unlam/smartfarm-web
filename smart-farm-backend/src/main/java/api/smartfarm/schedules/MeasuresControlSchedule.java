package api.smartfarm.schedules;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.DynamicConfiguration;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.notifications.NotificationType;
import api.smartfarm.models.entities.AverageMeasure;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.services.CropTypeService;
import api.smartfarm.services.FarmService;
import api.smartfarm.services.MeasureService;
import api.smartfarm.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static api.smartfarm.models.entities.AverageMeasure.AverageMeasureLevel;

@Component
public class MeasuresControlSchedule {

    private final FarmService farmService;
    private final CropTypeService cropTypeService;
    private final MeasureService measureService;
    private final NotificationService notificationService;
    private final DynamicConfiguration measuresCronConfiguration;

    @Autowired
    public MeasuresControlSchedule(
        FarmService farmService,
        CropTypeService cropTypeService,
        MeasureService measureService,
        NotificationService notificationService,
        DynamicConfiguration measuresCronConfiguration
    ) {
        this.farmService = farmService;
        this.cropTypeService = cropTypeService;
        this.measureService = measureService;
        this.notificationService = notificationService;
        this.measuresCronConfiguration = measuresCronConfiguration;
    }

    @Scheduled(cron = "#{@measuresCronInterval}")
    void checkFarmsMeasuresSchedule() {
        List<Farm> farms = farmService.findAll();
        farms.forEach(this::checkFarmMeasures);
    }

    private void checkFarmMeasures(Farm farm) {
        List<String> parametersOutOfRangeMessages = buildParameterOutOfRangeMessages(farm);

        boolean notificationSentRecently = notificationService.notificationSentRecently(
            farm.getId(),
            measuresCronConfiguration.getTimeToReSendNotification(),
            NotificationType.PARAMETER_OUT_OF_RANGE
        );

        if (!parametersOutOfRangeMessages.isEmpty() && !notificationSentRecently) {
            String body = String.join("\n", parametersOutOfRangeMessages);
            notificationService.sendParameterOutOfRange(body, farm);
        }
    }

    private List<String> buildParameterOutOfRangeMessages(Farm farm) {
        List<String> parametersOutOfRangeMessages = new ArrayList<>();
        List<Sector> farmSectors = farm.getSectors();

        farmSectors.forEach(sector -> {
            CropType cropType = cropTypeService.findById(sector.getCrop().getType());
            parametersOutOfRangeMessages.addAll(checkSectorSensorsValues(farm.getId(), sector, cropType));
        });

        return parametersOutOfRangeMessages;
    }

    private List<String> checkSectorSensorsValues(String farmId, Sector sector, CropType cropType) {
        List<AverageMeasure> averageMeasures = measureService.getAverageMeasures(farmId, sector.getSensors());
        return checkAverageMeasuresForCropType(averageMeasures, cropType);
    }

    private List<String> checkAverageMeasuresForCropType(List<AverageMeasure> averageMeasures, CropType cropType) {
        List<String> notOkValues = new ArrayList<>();
        averageMeasures.forEach(averageMeasure -> {
            AverageMeasureLevel measureLevel = cropType.checkAverageMeasure(averageMeasure);

            if (measureLevel == AverageMeasureLevel.BELOW_MIN) {
                averageMeasure.addBelowMinFor(cropType.getName());
            } else if (measureLevel == AverageMeasureLevel.OVER_MAX) {
                averageMeasure.addOverMaxFor(cropType.getName());
            }
            String msg = averageMeasure.getNotificationMsg();
            if (msg != null)
                notOkValues.add(msg);
        });
        return notOkValues;
    }

}