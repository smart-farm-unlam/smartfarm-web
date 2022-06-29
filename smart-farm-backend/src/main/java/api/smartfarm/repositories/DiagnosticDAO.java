package api.smartfarm.repositories;

import api.smartfarm.models.documents.Diagnostic;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticDAO extends MongoRepository<Diagnostic, String> {

}
