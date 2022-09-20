package api.smartfarm.repositories;

import api.smartfarm.models.documents.notifications.NotificationStatus;
import api.smartfarm.models.documents.notifications.ParameterOutOfRangeNotification;
import api.smartfarm.models.documents.notifications.SmartFarmNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationDAO extends MongoRepository<SmartFarmNotification, String> {

    List<SmartFarmNotification> findByFarmIdAndStatus(String farmId, NotificationStatus status);

    Optional<ParameterOutOfRangeNotification> findTop1ByFarmIdAndTypeAndStatusOrderByDateDesc(String farmId, String type, NotificationStatus status);

}
