package com.lalynk.lalynk_backend;

import java.util.List;

public interface IUserService {

    UserDTO createUser(CreateUserRequest userRequest);
    List<UserDTO> getAllUsers();


}
