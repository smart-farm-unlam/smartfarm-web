package api.smartfarm.repositories;

import api.smartfarm.models.documents.notifications.ParameterOutOfRangeNotification;
import api.smartfarm.models.documents.notifications.SmartFarmNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NotificationDAO extends MongoRepository<SmartFarmNotification, String> {

    List<SmartFarmNotification> findByFarmId(String farmId);

    @Query(value = "{'farmId':?0, '_class': ?1, 'status': ?2}", sort= "{'date':-1}")
    List<ParameterOutOfRangeNotification> findByFarmIdAndClassAndStatusOrderByDate(String farmId, String className, String status);

}
