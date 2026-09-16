package com.lalynk.lalynk_backend.users.internal;

import com.lalynk.lalynk_backend.users.IUserService;
import com.lalynk.lalynk_backend.users.UserDTO;
import com.lalynk.lalynk_backend.users.UserNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

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

    @Override
    public UUID findUserIdBySubject(String auth0Subject) {
        User user = userRepository.findByAuth0Subject(auth0Subject).orElseThrow(() -> new UserNotFoundException());
        return user.getId();
    }

    @Override
    public void getOrCreateUser(String auth0Subject, String email) {
        if(userRepository.existsByAuth0Subject(auth0Subject)) {
            return;
        }
        User user = new User(email, auth0Subject);
        userRepository.save(user);
    }


}
