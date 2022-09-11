package api.smartfarm.repositories;

import api.smartfarm.models.documents.DynamicConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DynamicConfigDAO extends MongoRepository<DynamicConfiguration, String> {
}
