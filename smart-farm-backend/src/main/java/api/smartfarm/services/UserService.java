package api.smartfarm.services;

import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.users.CreateUserRequestDTO;
import api.smartfarm.models.dtos.users.UpdateUserRequestDTO;
import api.smartfarm.models.dtos.users.UserResponseDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponseDTO create(CreateUserRequestDTO createUserRequest) {
        User user = new User(createUserRequest);

        userDAO.save(user);
        LOGGER.info("Saved user {} successfully", user);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO update(String id, UpdateUserRequestDTO updateUserRequest) {
        User user = getUserById(id);

        if (updateUserRequest.getFirstName() != null)
            user.setFirstName(updateUserRequest.getFirstName());

        if (updateUserRequest.getLastName() != null)
            user.setLastName(updateUserRequest.getLastName());

        if (updateUserRequest.getEmail() != null)
            user.setEmail(updateUserRequest.getEmail());

        if (updateUserRequest.getDeviceId() != null)
            user.addNewDevice(updateUserRequest.getDeviceId());

        userDAO.save(user);
        LOGGER.info("User {} updated successfully", id);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO getById(String id) {
        User user = getUserById(id);
        return new UserResponseDTO(user);
    }

    public User getUserById(String id) {
        LOGGER.info("Getting user with id {}", id);
        return userDAO.findById(id).orElseThrow(() -> {
            String errorMsg = "User with id " + id + " not exists on database";
            return new NotFoundException(errorMsg);
        });

    }
}
