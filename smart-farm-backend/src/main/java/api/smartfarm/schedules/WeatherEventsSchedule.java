package api.smartfarm.schedules;

import api.smartfarm.clients.weather.WeatherClient;
import api.smartfarm.clients.weather.model.DailyForecast;
import api.smartfarm.clients.weather.model.FutureForecastData;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.services.FarmService;
import api.smartfarm.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class WeatherEventsSchedule {

    private final FarmService farmService;
    private final WeatherClient weatherClient;
    private final NotificationService notificationService;

    private static final List<Integer> STORM_ICON_NUMBERS = Arrays.asList(15, 16, 17, 41, 42);
    private static final List<Integer> SNOW_ICON_NUMBERS = Arrays.asList(22, 23, 29, 44);
    private static final Integer ICE = 24;
    private static final Integer WINDY = 32;

    private static final Integer TOMORROW_INDEX = 1;

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
        FutureForecastData futureForecastData = weatherClient.getFutureForecast(farm.getLocationKey());

        if (!futureForecastData.getDailyForecasts().isEmpty()) {
            DailyForecast tomorrowForecast = futureForecastData.getDailyForecasts().get(TOMORROW_INDEX);
            String body = checkWeatherConditions(tomorrowForecast);

            if (body != null) {
                String title = buildTitle();
                notificationService.sendWeatherAlertCondition(title, body, farm);
            }
        }
    }

    private String buildTitle() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DayOfWeek day = tomorrow.getDayOfWeek();

        String tomorrowText = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
            .concat(" " + tomorrow.getDayOfMonth() + "/" + tomorrow.getMonthValue());

        return String.format(TITLE, tomorrowText);
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
        } else if (ICE == dayIcon) {
            alertMessage = "Probabilidad de granizo";
        }

        return alertMessage;
    }
}
