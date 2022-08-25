package api.smartfarm.controllers;

import api.smartfarm.models.dtos.users.CreateUserRequestDTO;
import api.smartfarm.models.dtos.users.UpdateUserRequestDTO;
import api.smartfarm.models.dtos.users.UserResponseDTO;
import api.smartfarm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody CreateUserRequestDTO createUserRequest) {
        return userService.create(createUserRequest);
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
}
