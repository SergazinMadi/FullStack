package org.example.fullstack.service;

import org.example.fullstack.db.dto.request.LoginRequest;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegistrationRequest request);
    AuthResponse login(LoginRequest loginRequest);
}
