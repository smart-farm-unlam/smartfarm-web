package api.smartfarm.services;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.dtos.SectorCropTypesDTO;
import api.smartfarm.models.dtos.SectorDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.CropTypeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorService {

    private final FarmService farmService;
    private final CropTypeDAO cropTypeDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SectorService.class);

    @Autowired
    public SectorService(FarmService farmService, CropTypeDAO cropTypeDAO) {
        this.farmService = farmService;
        this.cropTypeDAO = cropTypeDAO;
    }

    public List<SectorDTO> getSectors(String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors from farmId: {}", farmId);
        return farm.getSectors().stream().map(SectorDTO::new).collect(Collectors.toList());
    }

    public List<SectorCropTypesDTO> getSectorsCropTypes(String farmId) {
        Farm farm = farmService.getFarmById(farmId);
        LOGGER.info("Getting sectors + cropType from farmId: {}", farmId);
        return farm.getSectors().stream().map(sector -> {
            String id = sector.getCrop().getType();
            CropType cropType = cropTypeDAO.findById(id).orElseThrow(() -> {
                String errorMsg = "CropType with id " + id + " not exists on database";
                return new NotFoundException(errorMsg);
            });
            return new SectorCropTypesDTO(sector, cropType);
        }).collect(Collectors.toList());
    }
}
