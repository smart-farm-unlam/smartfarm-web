package api.smartfarm.repositories;

import api.smartfarm.models.documents.Diagnostic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiagnosticDAO extends MongoRepository<Diagnostic, String> {

    List<Diagnostic> findByFarmIdAndPlantIdOrderByDateTimeDesc(String farmId, String plantId);
}
