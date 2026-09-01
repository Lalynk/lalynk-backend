package com.lalynk.lalynk_backend.users.internal;

import com.lalynk.lalynk_backend.users.CreateUserRequest;
import com.lalynk.lalynk_backend.users.IUserService;
import com.lalynk.lalynk_backend.users.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        String passwordHash = passwordEncoder.encode(createUserRequest.password());
        User user = new User(createUserRequest.email(), passwordHash);
        User saved = userRepository.save(user);
        return new UserDTO(saved.getId(), saved.getPasswordHash(),saved.getEmail(), saved.getCreatedAt());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for(User u: users) {
            userDTOs.add(new UserDTO(u.getId(), u.getPasswordHash(), u.getEmail(), u.getCreatedAt()));
        }
        return userDTOs;
    }

    @Override
    public boolean doesExist(UUID userId) {
        return userRepository.existsById(userId);
    }


}
