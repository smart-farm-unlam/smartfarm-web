package api.smartfarm.repositories;

import api.smartfarm.models.documents.notifications.SmartFarmNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationDAO extends MongoRepository<SmartFarmNotification, String> {

    List<SmartFarmNotification> findByFarmId(String farmId);

}
