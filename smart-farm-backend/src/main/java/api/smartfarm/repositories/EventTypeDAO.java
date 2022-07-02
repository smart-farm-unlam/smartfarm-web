package api.smartfarm.repositories;

import api.smartfarm.models.documents.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventTypeDAO extends MongoRepository<EventType, String> {

}
