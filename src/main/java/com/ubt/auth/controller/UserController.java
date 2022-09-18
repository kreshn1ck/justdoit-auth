package com.ubt.auth.controller;

import com.ubt.auth.request.ForgotPasswordRequest;
import com.ubt.auth.request.RegistrationRequest;
import com.ubt.auth.request.ResetPasswordRequest;
import com.ubt.auth.service.authentication.AuthenticationService;
import com.ubt.auth.service.user.UserService;
import com.ubt.auth.transport.UserTransport;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    private final String DEFAULT_ENCODER = "UTF-8";

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/users/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        // LOGGER.info("Updating the user token: {}", forgotPasswordRequest.getEmail().substring(0, 5));
        userService.requestForgotPassword(forgotPasswordRequest.getEmail());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/users/reset-password/{token}")
    public void validateResetToken(@PathVariable("token") String token) throws Exception {
        // LOGGER.info("Getting user token: {}", token);
        userService.validateResetToken(URLDecoder.decode(token, DEFAULT_ENCODER));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/users/reset-password/{token}")
    public void resetUserPassword(@PathVariable("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {
        // LOGGER.info("Resetting user password with token: {}", token);
        userService.resetUserPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getPassword(), resetPasswordRequest.getConfirmPassword(), URLDecoder.decode(token, DEFAULT_ENCODER));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/users/user-confirmation/{token}")
    public void userConfirmation(@PathVariable("token") String token) throws Exception {
        // LOGGER.info("User confirmation with token: {}", token);
        userService.confirmUser(URLDecoder.decode(token, DEFAULT_ENCODER));
    }

    @PostMapping("/users/sign-up")
    public ResponseEntity<UserTransport> registerAccount(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        // LOGGER.info("Registering user");
        UserTransport userTransport = userService.registerUser(registrationRequest);
        return ResponseEntity.ok(userTransport);
    }

    /*@GetMapping("/api/users")
    public GetAllUsersByPageResponseDTO getUsers(@RequestParam Optional<Integer> page, @RequestParam Optional<String> name) {
        LOGGER.info("Retrieving all users");
        return userService.getUsers(page, name);
    }

    @GetMapping("/api/user")
    public User getAuthenticatedUser() {
        LOGGER.info("Getting authenticated user");
        return authenticationService.getAuthenticatedUser();
    }*/
}
