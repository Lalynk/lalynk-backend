package com.lalynk.lalynk_backend.users.internal;

import com.lalynk.lalynk_backend.users.IUserService;
import com.lalynk.lalynk_backend.users.UserAlreadyExistsException;
import com.lalynk.lalynk_backend.users.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDTO createUser(Authentication authentication) {

        String auth0Subject = authentication.getName();

        if(userRepository.existsByAuth0Subject(auth0Subject)) throw new UserAlreadyExistsException();

        String email = ((JwtAuthenticationToken) authentication).getToken().getClaimAsString("https://api.lalynk.com/email");



        User user = new User(email, auth0Subject);
        User saved = userRepository.save(user);
        return new UserDTO(saved.getId(),saved.getEmail(), saved.getCreatedAt());
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
    public boolean doesExist(UUID userId) {
        return userRepository.existsById(userId);
    }


}
