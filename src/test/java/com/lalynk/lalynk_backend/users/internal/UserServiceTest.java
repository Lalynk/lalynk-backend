package com.lalynk.lalynk_backend.users.internal;

import com.lalynk.lalynk_backend.users.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldGetCurrentUser() {

        String auth0Subject = "abc123";
        User user = new User("test@gmail.com", "abc123");
        when(userRepository.findByAuth0Subject(auth0Subject)).thenReturn(Optional.of(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(auth0Subject);

        UserDTO userDTO = userService.getCurrentUser(authentication);
        assertEquals("test@gmail.com", userDTO.email());
    }

}
