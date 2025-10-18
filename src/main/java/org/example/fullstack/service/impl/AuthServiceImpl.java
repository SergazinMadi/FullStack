package org.example.fullstack.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.db.dto.request.LoginRequest;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.dto.response.AuthResponse;
import org.example.fullstack.db.enums.UserRole;
import org.example.fullstack.db.mapper.UserMapper;
import org.example.fullstack.db.model.User;
import org.example.fullstack.db.repository.UserRepository;
import org.example.fullstack.exception.UserAlreadyExistsException;
import org.example.fullstack.service.AuthService;
import org.example.fullstack.service.UserService;
import org.example.fullstack.service.util.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = userMapper.registrationRequestToUser(request);
        user.setRole(UserRole.valueOf(request.role()));
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) throws IllegalArgumentException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        UserDetails userDetails = userService.loadUserByUsername(loginRequest.username());
        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt, "Аутентификация прошла успешно");
    }
}
