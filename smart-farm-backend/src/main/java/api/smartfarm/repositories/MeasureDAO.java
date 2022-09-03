package api.smartfarm.repositories;

import api.smartfarm.models.documents.Measure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface MeasureDAO extends MongoRepository<Measure, String> {

    List<Measure> findByFarmIdAndSensorCodeAndDateTimeGreaterThanEqualOrderByDateTime(String farmId, String sectorCode, Date dateTime);
}
