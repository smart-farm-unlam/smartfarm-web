package api.smartfarm.services;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.users.LoginUserRequestDTO;
import api.smartfarm.models.dtos.users.UpdateUserRequestDTO;
import api.smartfarm.models.dtos.users.UserResponseDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final FarmService farmService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserDAO userDAO, FarmService farmService) {
        this.userDAO = userDAO;
        this.farmService = farmService;
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

    public Optional<UserResponseDTO> login(LoginUserRequestDTO loginUserRequest) {
        LOGGER.info("Searching user with email {}", loginUserRequest.getEmail());

        User user = userDAO.findByEmail(loginUserRequest.getEmail()).orElse(null);

        if (user == null) {
            LOGGER.info("User with email {} not exists on database", loginUserRequest.getEmail());
            return Optional.empty();
        }
        LOGGER.info("User found: {}", user);

        Farm farm = farmService.getFarmByUserId(user.getId());
        return Optional.of(new UserResponseDTO(user, farm.getId()));
    }

    public UserResponseDTO createNewUser(LoginUserRequestDTO loginUserRequestDTO) {
        User user = new User(loginUserRequestDTO.getUsername(), loginUserRequestDTO.getEmail());

        userDAO.save(user);
        LOGGER.info("Create user {} successfully", user);

        LOGGER.info("Creating new farm for user {}", user.getEmail());
        Farm farm = farmService.create(user);

        return new UserResponseDTO(user, farm.getId());
    }

    public UserResponseDTO update(String id, UpdateUserRequestDTO updateUserRequest) {
        User user = getUserById(id);

        if (updateUserRequest.getUsername() != null)
            user.setUsername(updateUserRequest.getUsername());

        if (updateUserRequest.getEmail() != null)
            user.setEmail(updateUserRequest.getEmail());

        if (updateUserRequest.getDeviceId() != null)
            user.addNewDevice(updateUserRequest.getDeviceId());

        userDAO.save(user);
        LOGGER.info("User {} updated successfully", id);

        return new UserResponseDTO(user);
    }

}
