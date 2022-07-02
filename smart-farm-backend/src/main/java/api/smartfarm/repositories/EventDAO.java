package api.smartfarm.repositories;

import api.smartfarm.models.documents.events.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventDAO extends MongoRepository<Event,String> {
    @Query("{ 'endDate' : { $exists : false }, 'farmId': ?0, 'eventType': ?1 }")
    Optional<Event> findLastEventByFarmAndEventType(String farmId, String eventType);

    List<Event> findByFarmId(String farmId);
}
