package api.smartfarm.services;

import api.smartfarm.clients.accuweather.WeatherClient;
import api.smartfarm.clients.accuweather.model.LocationData;
import api.smartfarm.clients.accuweather.model.WeatherData;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.SensorType;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.WeatherResponseDTO;
import api.smartfarm.models.dtos.farms.CreateFarmRequestDTO;
import api.smartfarm.models.dtos.farms.FarmResponseDTO;
import api.smartfarm.models.dtos.farms.InitFarmRequestDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.FarmDAO;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FarmService {

    private final FarmDAO farmDAO;
    private final UserDAO userDAO;
    private final WeatherClient weatherClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmService.class);

    @Autowired
    public FarmService(FarmDAO farmDAO, UserDAO userDAO, WeatherClient weatherClient) {
        this.farmDAO = farmDAO;
        this.userDAO = userDAO;
        this.weatherClient = weatherClient;
    }

    public FarmResponseDTO create(CreateFarmRequestDTO createFarmRequestDTO) {
        Optional<User> user = userDAO.findById(createFarmRequestDTO.getUserId());
        if (!user.isPresent()) {
            String errorMsg = "User with id " + createFarmRequestDTO.getUserId() + "not exists on database";
            throw new NotFoundException(errorMsg);
        }

        Farm farm = new Farm(createFarmRequestDTO);
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

    public void initFarm(String id, InitFarmRequestDTO initRequest) {
        resetFarm(id);

        Farm farm = getFarmById(id);
        farm.setLength(initRequest.getLength());
        farm.setWidth(initRequest.getWidth());

        Double latitude = initRequest.getLatitude();
        Double longitude = initRequest.getLongitude();
        farm.setLatitude(latitude);
        farm.setLongitude(longitude);

        //Get location position from weatherClient
        LocationData locationData = weatherClient.geoPositionLocation(latitude, longitude);
        farm.setLocationKey(locationData.getKey());

        update(farm);
        LOGGER.info("Farm initialized successfully: {}", farm);
    }

    private void resetFarm(String id) {
        Farm farm = getFarmById(id);

        farm.setLength(null);
        farm.setWidth(null);
        farm.setSensors(new ArrayList<>());
        farm.setSectors(new ArrayList<>());
        farm.setEvents(new ArrayList<>());
        farm.setLatitude(null);
        farm.setLongitude(null);
        farm.setLocationKey(null);

        update(farm);
    }

    public WeatherResponseDTO getWeather(String id) {
        Farm farm = getFarmById(id);

        WeatherData weatherData = weatherClient.getCurrentWeatherConditions(farm.getLocationKey());

        String temperature = farm.getSensors().stream()
            .filter(it -> it.getSensorTypeId().equals(SensorType.SensorTypeId.AT))
            .findFirst()
            .map(it -> it.getLastMeasure().getValue() + "°C")
            .orElse(null);

        String humidity = farm.getSensors().stream()
            .filter(it -> it.getSensorTypeId().equals(SensorType.SensorTypeId.AH))
            .findFirst()
            .map(it -> it.getLastMeasure().getValue() + "%")
            .orElse(null);

        return new WeatherResponseDTO(weatherData, temperature, humidity);
    }

}
