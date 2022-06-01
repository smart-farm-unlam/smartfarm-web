package api.smartfarm.service;

import api.smartfarm.daos.FarmDAO;
import api.smartfarm.entities.EventParameter;
import api.smartfarm.entities.Farm;
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

//    public Double getParameter(String id, EventParameter parameter) {
//        Optional<Farm> farm = farmDAO.getById(id);
//        if(farm.isPresent()) {
//            switch (parameter) {
//                case TEMPERATURE: return farm.get().getTemperature();
//                default: return 0.0;
//            }
//        }
//    }
}
