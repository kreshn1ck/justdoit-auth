package com.ubt.auth.service.authentication;

import com.ubt.auth.enums.UserStatus;
import com.ubt.auth.exception.ValidationExceptionBuilder;
import com.ubt.auth.model.User;
import com.ubt.auth.repository.UserRepository;
import com.ubt.auth.service.refreshtoken.RefreshTokenService;
import com.ubt.auth.util.JwtUtil;
import com.ubt.auth.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, RefreshTokenService refreshTokenService,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationSuccess authenticate(String usernameOrEmail, String password) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        validateAuthentication(user, password);
        return getAuthenticationResponse(user);
    }

    private boolean passwordMatches(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    private AuthenticationSuccess getAuthenticationResponse(User user) {
        String authToken = JwtUtil.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        LOGGER.info("User authenticated successfully");
        return AuthenticationSuccess.of(user.getEmail(), AuthenticationSuccessReason.AUTHENTICATED, authToken, refreshToken, user);
    }

    private void validateAuthentication(User user, String password) throws ValidationException {
        if (user == null || !passwordMatches(password, user)) {
            LOGGER.info("Credentials wrong for login");
            throw ValidationExceptionBuilder.withErrorCodeAndAttributeAndError(
                    AuthenticationFailure.of(AuthenticationFailureReason.BAD_CREDENTIALS).getReason().name(),
                    "password",
                    "User with email / username and password does not exist");
        }
        else if (UserStatus.UNCONFIRMED.equals(user.getStatus())) {
            LOGGER.info("User is unconfirmed");
            throw ValidationExceptionBuilder.withErrorCodeAndAttributeAndError(
                    AuthenticationFailure.of(AuthenticationFailureReason.USER_NOT_CONFIRMED).getReason().name(),
                    "email",
                    "Email is not confirmed");
        }
    }

    /*public UserTransport getAuthenticatedUser() {
        String email = getAuthenticatedUserEmail();
        LOGGER.info("Getting authenticated user with email: {} ", email);
        UserTransport user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("User with email " + email + " does not exist");
        }
        return user;
    }*/
    /*public String getAuthenticatedUserEmail() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            LOGGER.info("Getting authenticated user email");
            return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        LOGGER.debug("No authenticated user present");
        throw new AuthenticationCredentialsNotFoundException("No authenticated user present");
    }

    public Long getAuthenticatedUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            LOGGER.info("Getting authenticated user account");
            return (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        }
        LOGGER.debug("No authenticated user present");
        return null;
    }*/
}
