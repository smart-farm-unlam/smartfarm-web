package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.SectorDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.FarmDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorService {

    private final FarmDAO farmDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);

    @Autowired
    public SectorService(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public List<SectorDTO> getSectorsById(String farmId) {
        Farm farm = farmDAO.findById(farmId).orElseThrow(() -> {
            LOGGER.error("Farm with farmId {} not exists on database", farmId);
            return new NotFoundException("Farm not exists on database");
        });
        return farm.getSectors().stream().map(SectorDTO::new).collect(Collectors.toList());
    }
}
