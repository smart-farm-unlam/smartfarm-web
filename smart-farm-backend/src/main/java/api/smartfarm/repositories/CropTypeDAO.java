package api.smartfarm.repositories;

import api.smartfarm.models.documents.CropType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CropTypeDAO extends MongoRepository<CropType, String> {

}
