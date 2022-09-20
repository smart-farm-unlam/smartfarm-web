package api.smartfarm.services;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.CropTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CropTypeService {

    private final CropTypeDAO cropTypeDAO;

    @Autowired
    public CropTypeService(CropTypeDAO cropTypeDAO) {
        this.cropTypeDAO = cropTypeDAO;
    }

    public CropType findById(String id) {
        return cropTypeDAO.findById(id).orElseThrow(() ->
            new NotFoundException("Crop type: " + id + " not found in database")
        );
    }

}
