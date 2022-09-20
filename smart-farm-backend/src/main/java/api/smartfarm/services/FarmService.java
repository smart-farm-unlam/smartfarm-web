package api.smartfarm.services;

import api.smartfarm.clients.weather.WeatherClient;
import api.smartfarm.clients.weather.model.FutureForecastData;
import api.smartfarm.clients.weather.model.LocationData;
import api.smartfarm.clients.weather.model.WeatherData;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.farms.CreateFarmRequestDTO;
import api.smartfarm.models.dtos.farms.FarmResponseDTO;
import api.smartfarm.models.dtos.farms.InitFarmRequestDTO;
import api.smartfarm.models.dtos.farms.UpdateFarmRequestDTO;
import api.smartfarm.models.dtos.weather.ForecastResponseDTO;
import api.smartfarm.models.dtos.weather.WeatherResponseDTO;
import api.smartfarm.models.entities.Sensor;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.EventDAO;
import api.smartfarm.repositories.FarmDAO;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmService {

    private final FarmDAO farmDAO;
    private final UserDAO userDAO;
    private final WeatherClient weatherClient;
    private final EventDAO eventDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmService.class);

    @Autowired
    public FarmService(
        FarmDAO farmDAO,
        UserDAO userDAO,
        WeatherClient weatherClient,
        EventDAO eventDAO
    ) {
        this.farmDAO = farmDAO;
        this.userDAO = userDAO;
        this.weatherClient = weatherClient;
        this.eventDAO = eventDAO;
    }

    public FarmResponseDTO create(CreateFarmRequestDTO createFarmRequest) {
        Optional<User> user = userDAO.findById(createFarmRequest.getUserId());
        if (!user.isPresent()) {
            String errorMsg = "User with id " + createFarmRequest.getUserId() + "not exists on database";
            throw new NotFoundException(errorMsg);
        }

        Farm farm = new Farm(createFarmRequest);
        farmDAO.save(farm);
        LOGGER.info("Farm created with id {}", farm.getId());

        return new FarmResponseDTO(farm);
    }

    //calcular si la ultima medición es de hace menos de 15 minutos y si es mayor
    //determinar que el microcontrolador esta desconectado del wifi
    public FarmResponseDTO getById(String id) {
        Farm farm = getFarmById(id);
        return new FarmResponseDTO(farm);
    }

    public FarmResponseDTO update(String id, UpdateFarmRequestDTO updateFarmRequest) {
        Farm farm = getFarmById(id);
        setLocation(farm, updateFarmRequest.getLatitude(), updateFarmRequest.getLongitude());

        //Update farm
        update(farm);

        return new FarmResponseDTO(farm);
    }

    public Farm getFarmById(String id) {
        LOGGER.info("Getting farm with id {}", id);
        return farmDAO.findById(id).orElseThrow(() -> {
            String errorMsg = "Farm with id " + id + " not exists on database";
            return new NotFoundException(errorMsg);
        });
    }

    public void update(Farm farm) {
        farmDAO.save(farm);
        LOGGER.info("Farm {} updated successfully", farm.getId());
    }

    public void initFarm(String id, InitFarmRequestDTO initFarmRequest) {
        resetFarm(id);

        Farm farm = getFarmById(id);
        farm.setLength(initFarmRequest.getLength());
        farm.setWidth(initFarmRequest.getWidth());

        setLocation(farm, initFarmRequest.getLatitude(), initFarmRequest.getLongitude());

        update(farm);
        LOGGER.info("Farm initialized successfully: {}", farm);
    }

    private void resetFarm(String id) {
        Farm farm = getFarmById(id);

        farm.setLength(null);
        farm.setWidth(null);

        //We have to unlink sensors to sectors
        List<Sensor> sectorSensors = farm.getSectors().stream()
            .flatMap(sector -> sector.getSensors().stream())
            .collect(Collectors.toList());

        farm.getSensors().addAll(sectorSensors);

        //Delete sectors
        farm.setSectors(new ArrayList<>());

        //Delete all events associate with this farm
        eventDAO.deleteAllByFarmId(id);
        farm.setEvents(new ArrayList<>());

        //Clean latitude, longitude y location key
        farm.setLatitude(null);
        farm.setLongitude(null);
        farm.setLocationKey(null);

        //update farm
        update(farm);
    }

    public WeatherResponseDTO getWeather(String id) {
        Farm farm = getFarmById(id);
        WeatherData weatherData = weatherClient.getCurrentWeatherConditions(farm.getLocationKey());
        return new WeatherResponseDTO(weatherData);
    }

    public List<ForecastResponseDTO> futureWeather(String id) {
        Farm farm = getFarmById(id);
        FutureForecastData forecastData = weatherClient.getFutureForecast(farm.getLocationKey());

        return forecastData.getDailyForecasts().stream()
            .map(ForecastResponseDTO::new)
            .collect(Collectors.toList());
    }

    private void setLocation(Farm farm, Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            farm.setLatitude(latitude);
            farm.setLongitude(longitude);
            //Get location position from weatherClient
            LocationData locationData = weatherClient.geoPositionLocation(latitude, longitude);
            if (locationData != null) {
                farm.setLocationKey(locationData.getKey());
            }
        }
    }

    public List<Farm> findAll() {
        return farmDAO.findAll();
    }

}
