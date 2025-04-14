package com.akrisht.expenseTracker.service;

import com.akrisht.expenseTracker.entity.User;
import com.akrisht.expenseTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.register(user);

        assertTrue(registeredUser.isActive());
        assertNotEquals("password", registeredUser.getPassword()); // password should be encoded
        assertTrue(passwordEncoder.matches("password", registeredUser.getPassword()));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterFailsIfEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(user));
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User loggedInUser = userService.login("test@example.com", rawPassword);

        assertEquals("test@example.com", loggedInUser.getEmail());
    }

    @Test
    void testLoginFailsIfUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login("notfound@example.com", "any"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoginFailsIfPasswordWrong() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("correct_password"));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login("test@example.com", "wrong_password"));
        assertEquals("Invalid password", exception.getMessage());
    }
}
