package com.ubt.auth.controller;

import com.ubt.auth.request.UserAuthenticationRequest;
import com.ubt.auth.service.authentication.AuthenticationSuccess;
import com.ubt.auth.service.refreshtoken.RefreshTokenSuccess;
import com.ubt.auth.service.authentication.AuthenticationService;
import com.ubt.auth.service.refreshtoken.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationSuccess> login(@RequestBody UserAuthenticationRequest request) {
        AuthenticationSuccess authentication = authenticationService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(authentication);
    }

    @GetMapping("/refresh-token/{token}")
    public ResponseEntity<RefreshTokenSuccess> refreshToken(@PathVariable("token") String refreshToken) {
        RefreshTokenSuccess response = refreshTokenService.renewAuthenticationFromRefreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}