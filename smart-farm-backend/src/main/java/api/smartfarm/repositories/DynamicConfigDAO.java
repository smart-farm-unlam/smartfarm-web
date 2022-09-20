package api.smartfarm.repositories;

import api.smartfarm.models.documents.DynamicConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DynamicConfigDAO extends MongoRepository<DynamicConfiguration, String> {

    @Query("{ 'cronType': ?0 }")
    DynamicConfiguration findByCronType(String cronType);
}
