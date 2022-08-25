package api.smartfarm.repositories;

import api.smartfarm.models.documents.Measure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MeasureDAO extends MongoRepository<Measure, String> {

    List<Measure> findByFarmIdAndSensorCodeAndDateTimeGreaterThanEqual(String farmId, String sectorCode, Date dateTime);
}
