package api.smartfarm.services;

import api.smartfarm.models.documents.User;
import api.smartfarm.models.dtos.UserDTO;
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

    public UserDTO create(UserDTO userDTO) {
        User user = new User(userDTO);

        userDAO.save(user);
        LOGGER.info("Saved user {} successfully", user);

        userDTO.setId(user.getId());
        return userDTO;
    }
}
