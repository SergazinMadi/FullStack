package org.example.fullstack.db.dto.request;

import jakarta.persistence.EnumType;
import lombok.Data;
import org.example.fullstack.db.enums.UserRole;

public record RegistrationRequest (
    String username,
    String email,
    String password,
    String firstName,
    String lastName,
    String role
){}
