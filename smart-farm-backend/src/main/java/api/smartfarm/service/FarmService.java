package api.smartfarm.service;

import api.smartfarm.daos.FarmDAO;
import api.smartfarm.entities.events.AmbientHumidity;
import api.smartfarm.entities.events.EarthHumidity;
import api.smartfarm.entities.events.EventParameter;
import api.smartfarm.entities.Farm;
import api.smartfarm.entities.events.EarthTemperature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmService {

    private final FarmDAO farmDAO;

    @Autowired
    public FarmService(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public Optional<Farm> getById(String id) {
        return farmDAO.getById(id);
    }

    public Double getParameter(String id, EventParameter parameter) {
        if (parameter == null) {
            return null;
        }
        Optional<Farm> farm = farmDAO.getById(id);
        if (farm.isPresent()) {
            if (parameter instanceof EarthTemperature) {
                return farm.get().getEarthTemperature();
            } else if (parameter instanceof EarthHumidity) {
                return farm.get().getEarthHumidity();
            } else if (parameter instanceof AmbientHumidity){
                return farm.get().getAmbientHumidity();
            } else {
                return 0.0;
            }
        }
        return null;
    }

    public void save(Farm farm) {
    }
}
