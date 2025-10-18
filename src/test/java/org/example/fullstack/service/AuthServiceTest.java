package org.example.fullstack.service;

import org.example.fullstack.db.dto.request.LoginRequest;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.dto.response.AuthResponse;
import org.example.fullstack.db.enums.UserRole;
import org.example.fullstack.db.mapper.UserMapper;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.exception.UserAlreadyExistsException;
import org.example.fullstack.service.impl.AuthServiceImpl;
import org.example.fullstack.service.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.CLIENT);

        registrationRequest = new RegistrationRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test",
                "User",
                "CLIENT"
        );

        loginRequest = new LoginRequest(
                "testuser",
                "test@example.com",
                "password123"
        );
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userMapper.registrationRequestToUser(any(RegistrationRequest.class))).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.register(registrationRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Authentication successful", response.getMessage());
        
        verify(userRepository).findByUsername("testuser");
        verify(userMapper).registrationRequestToUser(registrationRequest);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(testUser);
        verify(jwtService).generateToken(testUser);
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.register(registrationRequest);
        });

        verify(userRepository).findByUsername("testuser");
        verify(userMapper, never()).registrationRequestToUser(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // Given
        UserDetails userDetails = testUser;
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Аутентификация прошла успешно", response.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername("testuser");
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void login_ShouldThrowException_WhenAuthenticationFails() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
    }
}
