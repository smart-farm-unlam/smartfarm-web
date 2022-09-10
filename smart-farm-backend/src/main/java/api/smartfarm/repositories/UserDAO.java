package api.smartfarm.repositories;

import api.smartfarm.models.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDAO extends MongoRepository<User, String> {

    Optional<User> findByEmail(String username);
}
