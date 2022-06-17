package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.FarmDAO;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmService {

    private final FarmDAO farmDAO;
    private final UserDAO userDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmService.class);

    @Autowired
    public FarmService(FarmDAO farmDAO, UserDAO userDAO) {
        this.farmDAO = farmDAO;
        this.userDAO = userDAO;
    }

    public FarmDTO create(FarmDTO farmDTO) {
        Optional<User> user = userDAO.findById(farmDTO.getUserId());
        if (!user.isPresent()) {
            LOGGER.error("User with {} not exists on database", farmDTO.getUserId());
            throw new NotFoundException("User not exists on database");
        }

        Farm farm = new Farm(farmDTO);
        farmDAO.save(farm);
        LOGGER.info("Saved farm {} successfully", farm);

        farmDTO.setId(farm.getId());
        farmDTO.setSensors(farm.getSensors());
        farmDTO.setSectors(farm.getSectors());
        farmDTO.setEvents(farm.getEvents());
        return farmDTO;
    }

    //calcular si la ultima medición es de hace menos de 15 minutos y si es mayor
    //determinar que el microcontrolador esta desconectado del wifi
    public FarmDTO getById(String id) {
        Farm farm = farmDAO.findById(id).orElseThrow(() -> {
            LOGGER.error("Farm with id {} not exists on database", id);
            return new NotFoundException("Farm not exists on database");
        });

        return new FarmDTO(farm);
    }

}
