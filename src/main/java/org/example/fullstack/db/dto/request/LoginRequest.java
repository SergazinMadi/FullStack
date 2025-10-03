package org.example.fullstack.db.dto.request;

import lombok.Data;

public record LoginRequest (
    String username,
    String email,
    String password
){}
