package api.smartfarm.schedules;

import api.smartfarm.models.documents.DynamicConfiguration;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.Measure;
import api.smartfarm.models.documents.notifications.NotificationType;
import api.smartfarm.services.FarmService;
import api.smartfarm.services.MeasureService;
import api.smartfarm.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class DisconnectionControlSchedule {

    private final FarmService farmService;
    private final MeasureService measureService;
    private final NotificationService notificationService;
    private final DynamicConfiguration disconnectionCronConfiguration;

    @Autowired
    public DisconnectionControlSchedule(
        FarmService farmService,
        MeasureService measureService,
        NotificationService notificationService,
        DynamicConfiguration disconnectionCronConfiguration
    ) {
        this.farmService = farmService;
        this.measureService = measureService;
        this.notificationService = notificationService;
        this.disconnectionCronConfiguration = disconnectionCronConfiguration;
    }

    @Scheduled(cron = "#{@disconnectControlControlCronInterval}")
    void checkFarmsDisconnectionSchedule() {
        List<Farm> farms = farmService.findAll();
        farms.forEach(this::checkForDisconnection);
    }

    private void checkForDisconnection(Farm farm) {
        if(!disconnectionCronConfiguration.isScheduleRunEnabled())
            return;

        Measure lastMeasure = measureService.getLastMeasureByFarmId(farm.getId());
        if(lastMeasure!=null && shouldNotify(farm.getId(),lastMeasure.getDateTime())){
            notificationService.sendFarmDisconnectedNotification(farm,lastMeasure.getDateTime());
        }
        //Si no tiene mediciones no notifico porque puede que no haya vinculado los sensores
    }

    private boolean shouldNotify(String farmId, Date lastMeasureDateTime) {
        boolean maxElapsedTimeReached = checkForElapsedTimeSinceLastMeasure(lastMeasureDateTime);
        if(maxElapsedTimeReached){
            return !notificationService.notificationSentRecently(
                    farmId,
                    disconnectionCronConfiguration.getTimeToReSendNotification(),
                    NotificationType.FARM_DISCONNECTED_NOTIFICATION);
        }
        return false;
    }

    private boolean checkForElapsedTimeSinceLastMeasure(Date lastMeasureDateTime) {
        long maxElapsedTime = disconnectionCronConfiguration.getMaxElapsedTime();

        Date currentDate = new Date();
        long elapsedTime = ChronoUnit.MINUTES.between(lastMeasureDateTime.toInstant(), currentDate.toInstant());
        return elapsedTime  >= maxElapsedTime;
    }
}