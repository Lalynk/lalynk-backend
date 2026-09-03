package com.lalynk.lalynk_backend.users;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    UserDTO createUser(CreateUserRequest userRequest, Authentication authentication);
    List<UserDTO> getAllUsers();
    boolean doesExist(UUID userId);


}
