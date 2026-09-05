package com.lalynk.lalynk_backend.users;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserDTO createUser(Authentication authentication);
    List<UserDTO> getAllUsers();
    UUID findUserIdBySubject(String auth0Subject);
    UserDTO getCurrentUser(Authentication authentication);
}
