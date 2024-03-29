package api.smartfarm.repositories;

import api.smartfarm.models.documents.events.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventDAO extends MongoRepository<Event, String> {
    @Query("{ 'endDate' : { $exists : false }, 'farmId': ?0, 'eventType': ?1 }")
    Optional<Event> findLastEventByFarmAndEventType(String farmId, String eventType);

    @Query("{ 'endDate' : { $exists : false }, 'farmId': ?0, 'sectorId': ?1, 'eventType': ?2 }")
    Optional<Event> findLastEventByFarmAndSectorAndEventType(String farmId, String sectorId, String eventType);

    List<Event> findByFarmId(String farmId);

    @Query("{ 'farmId': ?0, 'eventType': ?1, 'sectorId': ?2 }")
    List<Event> findEventsByFarmIdAndEventTypeAndSectorId(String farmId, String eventType, String sectorId);

    @Query("{ 'farmId': ?0, 'eventType': ?1 }")
    List<Event> findEventsByFarmIdAndEventType(String farmId, String eventType);

    @Query("{ 'farmId': ?0, 'sectorId': ?1 }")
    List<Event> findByFarmIdAndSectorId(String farmId, String sectorId);

    void deleteAllByFarmId(String farmId);
}