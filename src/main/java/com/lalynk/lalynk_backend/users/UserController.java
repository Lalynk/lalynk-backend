package com.lalynk.lalynk_backend.users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<UserDTO> createUser(Authentication authentication) {
        return new ResponseEntity<>(userService.createUser(authentication), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('users:read')")
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        return new ResponseEntity<>(userService.getCurrentUser(authentication), HttpStatus.OK);
    }


}
