package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.FarmDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    private final FarmDAO farmDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(FarmService.class);

    @Autowired
    public SectorService(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public List<Sector> getSectorsById(String id) {
        Farm farm = farmDAO.findById(id).orElseThrow(() -> {
            LOGGER.error("Farm with id {} not exists on database", id);
            return new NotFoundException("Farm not exists on database");
        });
        FarmDTO farmDTO = new FarmDTO(farm);
        return farmDTO.getSectors();
    }
}
