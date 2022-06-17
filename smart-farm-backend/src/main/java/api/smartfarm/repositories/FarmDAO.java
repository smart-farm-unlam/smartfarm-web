package api.smartfarm.repositories;

import api.smartfarm.models.documents.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmDAO extends MongoRepository<Farm, String> {

}
