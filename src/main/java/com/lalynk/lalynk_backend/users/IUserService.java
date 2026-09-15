package com.lalynk.lalynk_backend.users;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UUID findUserIdBySubject(String auth0Subject);
    void getOrCreateUser(String auth0Subject, String email);
}
