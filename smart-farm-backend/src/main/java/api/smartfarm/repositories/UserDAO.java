package api.smartfarm.repositories;

import api.smartfarm.models.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDAO extends MongoRepository<User, String> {
}
