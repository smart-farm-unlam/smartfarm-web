package api.smartfarm.repositories;

import api.smartfarm.models.documents.DiagnosticType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagnosticTypeDAO extends MongoRepository<DiagnosticType, String> {
}
