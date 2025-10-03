package org.example.fullstack.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstack.service.util.ReceiveToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final ReceiveToken receiveToken;

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Это публичный эндпоинт, доступный всем");
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> userProfile(Principal principal) {
        Map<String, String> tokenData = receiveToken.tokenData();
        return ResponseEntity.ok(tokenData);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Это эндпоинт только для администраторов");
    }
}
