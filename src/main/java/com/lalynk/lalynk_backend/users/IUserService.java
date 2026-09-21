package com.lalynk.lalynk_backend.users;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UUID findUserIdBySubject(String auth0Subject);
    void getOrCreateUser(String auth0Subject, String email);
}
