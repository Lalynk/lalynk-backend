package com.lalynk.lalynk_backend.users.internal;

import com.lalynk.lalynk_backend.CreateUserRequest;
import com.lalynk.lalynk_backend.IUserService;
import com.lalynk.lalynk_backend.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {


    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.email(), createUserRequest.password());
        User saved = userRepository.save(user);
        return new UserDTO(saved.getId(), saved.getEmail(), saved.getCreatedAt());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for(User u: users) {
            userDTOs.add(new UserDTO(u.getId(), u.getEmail(), u.getCreatedAt()));
        }
        return userDTOs;
    }


}
