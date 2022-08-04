package api.smartfarm.clients.accuweather;

import api.smartfarm.clients.accuweather.model.LocationData;
import api.smartfarm.clients.accuweather.model.WeatherData;
import api.smartfarm.models.exceptions.FailedDependencyException;
import api.smartfarm.models.exceptions.LimitReachException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class WeatherClient {

    @Value("${accuweather.api.host}")
    private String host;

    @Value("${accuweather.api.key}")
    private String apiKey;

    @Value("${accuweather.api.key2}")
    private String apiKey2;

    private final RestTemplate restTemplate;

    private static final String GEO_POSITION_URL = "%s/locations/v1/cities/geoposition/search?" +
        "apikey=%s&q=%s,%s&language=es-ar&details=false&toplevel=true";

    private static final String CURRENT_WEATHER_URL = "%s/currentconditions/v1/%s?" +
        "apikey=%s&language=es-ar&details=true";

    private static final String ERROR_WEATHER_MESSAGE = "Failed to obtain current weather from Weather Client";

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherClient.class);
    private static final ParameterizedTypeReference<List<WeatherData>> WEATHER_DATA_RESPONSE =
        new ParameterizedTypeReference<List<WeatherData>>() {
        };

    @Autowired
    public WeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationData geoPositionLocation(Double latitude, Double longitude) {
        LocationData locationData;
        try {
            locationData = geoPositionLocation(latitude, longitude, apiKey);
        } catch (LimitReachException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.info("Trying to get geoPosition location with another apiKey");
            locationData = geoPositionLocation(latitude, longitude, apiKey2);
        }
        return locationData;
    }

    public LocationData geoPositionLocation(Double latitude, Double longitude, String apiKey) {
        String geoPositionUrl = String.format(
            GEO_POSITION_URL,
            host,
            apiKey,
            latitude.toString(),
            longitude.toString()
        );

        LocationData locationData = null;
        try {
            locationData = restTemplate.getForObject(geoPositionUrl, LocationData.class);
            LOGGER.info("LocationData for latitude and longitude ({},{}): {}", latitude, longitude, locationData);
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            throw new LimitReachException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to obtain location data from Weather Client");
        }

        return locationData;
    }

    public WeatherData getCurrentWeatherConditions(@NonNull String locationKey) {
        WeatherData weatherData;
        try {
            weatherData = getCurrentWeatherConditions(locationKey, apiKey);
        } catch (LimitReachException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.info("Trying to get weather conditions with another apiKey");
            weatherData = getCurrentWeatherConditions(locationKey, apiKey2);
        }
        return weatherData;
    }

    private WeatherData getCurrentWeatherConditions(String locationKey, String apiKey) {
        String currentWeatherUrl = String.format(
            CURRENT_WEATHER_URL,
            host,
            locationKey,
            apiKey
        );

        try {
            ResponseEntity<List<WeatherData>> response = restTemplate.exchange(
                currentWeatherUrl,
                HttpMethod.GET,
                null,
                WEATHER_DATA_RESPONSE
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<WeatherData> weatherData = response.getBody();
                LOGGER.info("WeatherData for key[{}]: {}", locationKey, weatherData);

                if (weatherData == null || weatherData.isEmpty()) return null;

                return weatherData.stream().findFirst().orElseThrow(
                    () -> new FailedDependencyException(ERROR_WEATHER_MESSAGE)
                );
            }

            throw new FailedDependencyException(ERROR_WEATHER_MESSAGE);
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            throw new LimitReachException(e.getMessage());
        } catch (Exception e) {
            throw new FailedDependencyException(ERROR_WEATHER_MESSAGE);
        }
    }

}
