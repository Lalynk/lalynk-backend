package com.lalynk.lalynk_backend.users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/me")
    public Object me(Authentication authentication) {
        return authentication.getPrincipal();
    }


}
