package com.lalynk.lalynk_backend.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    UserDTO createUser(CreateUserRequest userRequest);
    List<UserDTO> getAllUsers();
    boolean doesExist(UUID userId);


}
