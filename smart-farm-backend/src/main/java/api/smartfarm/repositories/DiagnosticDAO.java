package api.smartfarm.repositories;

import api.smartfarm.models.documents.Diagnostic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagnosticDAO extends MongoRepository<Diagnostic, String> {

}
