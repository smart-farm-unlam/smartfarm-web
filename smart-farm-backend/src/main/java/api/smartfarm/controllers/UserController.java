package api.smartfarm.controllers;

import api.smartfarm.models.dtos.users.LoginUserRequestDTO;
import api.smartfarm.models.dtos.users.UpdateUserRequestDTO;
import api.smartfarm.models.dtos.users.UserResponseDTO;
import api.smartfarm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO updateUser(
        @PathVariable String id,
        @RequestBody UpdateUserRequestDTO updateUserRequest
    ) {
        return userService.update(id, updateUserRequest);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable String id) {
        return userService.getById(id);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginUserRequestDTO loginUserRequest) {
        Optional<UserResponseDTO> userResponse = userService.login(loginUserRequest);

        if (userResponse.isPresent()) {
            return ResponseEntity.ok(userResponse.get());
        } else {
            UserResponseDTO body = userService.createNewUser(loginUserRequest);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        }
    }
}
