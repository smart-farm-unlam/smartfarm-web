package api.smartfarm.repositories;

import api.smartfarm.models.documents.MockedValue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MockValueDAO extends MongoRepository<MockedValue, String> {

}
