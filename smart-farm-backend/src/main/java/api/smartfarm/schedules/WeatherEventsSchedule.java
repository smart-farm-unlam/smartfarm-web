package api.smartfarm.schedules;

import api.smartfarm.clients.weather.WeatherClient;
import api.smartfarm.clients.weather.model.DailyForecast;
import api.smartfarm.clients.weather.model.FutureForecastData;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.services.FarmService;
import api.smartfarm.services.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class WeatherEventsSchedule {

    private final FarmService farmService;
    private final WeatherClient weatherClient;
    private final NotificationService notificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherEventsSchedule.class);

    private static final List<Integer> STORM_ICON_NUMBERS = Arrays.asList(15, 16, 17, 41, 42);
    private static final List<Integer> SNOW_ICON_NUMBERS = Arrays.asList(22, 23, 29, 44);
    private static final Integer ICE = 24;
    private static final Integer WINDY = 32;

    private static final String TITLE = "Alerta pronóstico %s";

    @Autowired
    public WeatherEventsSchedule(FarmService farmService, WeatherClient weatherClient, NotificationService notificationService) {
        this.farmService = farmService;
        this.weatherClient = weatherClient;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "#{@weatherCronInterval}")
    void checkWeatherConditionsSchedule() {
        List<Farm> farms = farmService.findAll();
        farms.stream().filter(it -> it.getLocationKey() != null)
            .forEach(this::alertWeatherConditions);
    }

    private void alertWeatherConditions(Farm farm) {
        LOGGER.info("Checking future weather events for farmId {}", farm.getId());
        FutureForecastData futureForecastData = weatherClient.getFutureForecast(farm.getLocationKey());

        futureForecastData.getDailyForecasts().forEach(it -> {
            String body = checkWeatherConditions(it);

            if (body != null) {
                String title = buildTitle(it.getDate());
                notificationService.sendWeatherAlertCondition(title, body, farm);
            }
        });
    }

    private String buildTitle(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek day = localDate.getDayOfWeek();

        String tomorrowText = day.getDisplayName(TextStyle.FULL, new Locale("es", "AR"))
            .concat(" " + localDate.getDayOfMonth() + "/" + localDate.getMonthValue());

        return String.format(TITLE, StringUtils.capitalize(tomorrowText));
    }

    private String checkWeatherConditions(DailyForecast dailyForecast) {
        String alertMessage = null;

        int dayIcon = dailyForecast.getDay().getIcon();
        int nightIcon = dailyForecast.getNight().getIcon();

        if (STORM_ICON_NUMBERS.contains(dayIcon) || STORM_ICON_NUMBERS.contains(nightIcon)) {
            alertMessage = "Tormentas fuertes";
        } else if (SNOW_ICON_NUMBERS.contains(dayIcon) || SNOW_ICON_NUMBERS.contains(nightIcon)) {
            alertMessage = "Nieve";
        } else if (WINDY == dayIcon || WINDY == nightIcon) {
            alertMessage = "Fuertes vientos";
        } else if (ICE == dayIcon || ICE == nightIcon) {
            alertMessage = "Probabilidad de granizo";
        }

        return alertMessage;
    }
}
