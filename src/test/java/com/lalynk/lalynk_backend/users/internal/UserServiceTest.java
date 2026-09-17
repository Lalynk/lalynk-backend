package com.lalynk.lalynk_backend.users.internal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void shouldCreateUserIfDoesNotExist() {
        String auth0Subject = "abc|123";
         String email = "abc@example.com";
         when(userRepository.existsByAuth0Subject(auth0Subject)).thenReturn(false);
         User user = new User(email, auth0Subject);
         when(userRepository.save(any(User.class))).thenReturn(user);

         userService.getOrCreateUser(auth0Subject, email);

         verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldNotCreateUserIfUserAlreadyExists() {
        String auth0Subject = "abc|123";
        String email = "abc@example.com";
        when(userRepository.existsByAuth0Subject(auth0Subject)).thenReturn(true);

        userService.getOrCreateUser(auth0Subject, email);

        verify(userRepository, never()).save(any(User.class));
    }

}
