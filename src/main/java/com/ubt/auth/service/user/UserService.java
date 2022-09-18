package com.ubt.auth.service.user;

import com.ubt.auth.config.MessagingService;
import com.ubt.auth.enums.UserStatus;
import com.ubt.auth.exception.ValidationExceptionBuilder;
import com.ubt.auth.mapper.UserTransportMapper;
import com.ubt.auth.model.User;
import com.ubt.auth.repository.UserRepository;
import com.ubt.auth.request.RegistrationRequest;
import com.ubt.auth.service.external.BackendService;
import com.ubt.auth.service.external.EmailService;
import com.ubt.auth.transport.UserTransport;
import com.ubt.auth.util.DateUtil;
import com.ubt.auth.util.TokenEncryption;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final BackendService backendService;
    private final MessagingService messagingService;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public void requestForgotPassword(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String userToken = user.getResetToken();
            if (userToken != null) {
                String decryptedToken = TokenEncryption.decrypt(userToken);
                try {
                    Date tokenDate = TokenEncryption.getDateBySplit(decryptedToken);
                    Date addedDate = DateUtil.addMinutesToDate(tokenDate, 5);
                    long differenceInMinutes = DateUtil.differenceInMinutes(addedDate);
                    if (differenceInMinutes >= 0) {
                        // throw ValidationExceptionBuilder.withErrorCode(ForgotPasswordFailureReason.USER_CANNOT_SEND_REQUEST.name());
                    }
                } catch (ParseException e) {
                    throw new ParseException("Cannot parse Date", 0);
                }
            }
            setResetToken(user);
            try {
                log.info("CALLING EMAIL SERVICE");
                emailService.sendForgotPasswordEmail(user, userToken);
                // LOGGER.debug("User with id: {} token has been sent to email: {}", optionalUser.get().getId(), email);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // LOGGER.debug("User with id: {} token has been sent to email: {}", optionalUser.get().getId(), email);
            }
            return;
        }
        // LOGGER.debug("Invalid email submitted {}", email);
        throw ValidationExceptionBuilder.withErrorCode(ForgotPasswordFailureReason.USER_INVALID_EMAIL_SUBMITTED.name());
    }

    public void validateResetToken(String resetToken) throws Exception {
        User user = userRepository.findByResetToken(resetToken);
        validateResetToken(user, resetToken);
    }

    @Transactional
    public void resetUserPassword(String email, String password, String confirmPassword, String token) throws Exception {
        User user = userRepository.findByResetToken(token);
        validateResetToken(user, token);
        if (!user.getEmail().equals(email)) {
            // LOGGER.debug("User with id: {} invalid email submitted {}", userOptional.get().getId(), email);
            throw ValidationExceptionBuilder.withErrorCode(ResetPasswordFailureReason.INVALID_EMAIL.name());
        }
        if (!password.equals(confirmPassword) || password.isEmpty()) {
            // LOGGER.debug("User with id: {} invalid password submitted", userOptional.get().getId());
            throw ValidationExceptionBuilder.withErrorCode(ResetPasswordFailureReason.CONFIRM_PASSWORD_DOES_NOT_MATCH.name());
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setUpdatedAt(new Date());
        // LOGGER.info("User with id: {} password updated successfully", userOptional.get().getId());
        userRepository.save(user);
    }

    @Transactional
    public void confirmUser(String token) throws Exception {
        User user = userRepository.findByConfirmToken(token);
        if (user == null) {
            // LOGGER.debug("User with token: {} not found", token);
            throw ValidationExceptionBuilder.withErrorCode(UserConfirmFailureReason.INVALID_CONFIRM_TOKEN.name());
        }
        String decryptedToken = TokenEncryption.decrypt(token);
        try {
            Date expiryDate = DateUtil.addHoursToDate(TokenEncryption.getDate(decryptedToken), 48);
            if (expiryDate.before(new Date())) {
                // LOGGER.debug("User with id: {} token expired", optionalUser.get().getId());
                throw ValidationExceptionBuilder.withErrorCode(UserConfirmFailureReason.CONFIRM_TOKEN_EXPIRED.name());
            }
        } catch (ParseException e) {
            // LOGGER.debug("Cannot parse date");
            throw new ParseException("Cannot parse Date", 0);
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    @Transactional
    public UserTransport registerUser(RegistrationRequest registrationRequest) throws Exception {
        User userByEmail = userRepository.findByEmail(registrationRequest.getEmail());
        if (userByEmail != null) {
            throw ValidationExceptionBuilder.withErrorCode(UserRegisterFailureReason.EMAIL_ALREADY_EXISTS.name());
        }
        User userByUsername = userRepository.findByUsername(registrationRequest.getEmail());
        if (userByUsername != null) {
            throw ValidationExceptionBuilder.withErrorCode(UserRegisterFailureReason.USERNAME_ALREADY_EXISTS.name());
        }
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw ValidationExceptionBuilder.withErrorCode(UserRegisterFailureReason.CONFIRM_PASSWORD_DOES_NOT_MATCH.name());
        }
        User user = createUser(registrationRequest);
        UserTransport userTransport = UserTransportMapper.toTransport(user);
        backendService.createUser(user);
        // emailService.sendUserConfirmationEmail(user, user.getConfirmToken());
        messagingService.sendUserCreatedMessage(userTransport);
        return userTransport;
    }

    private void validateResetToken(User user, String resetToken) throws Exception {
        if (user == null) {
            // LOGGER.debug("User with id: {} not found", optionalUser.get().getId());
            throw ValidationExceptionBuilder.withErrorCode(ResetPasswordFailureReason.INVALID_RESET_TOKEN.name());
        }
        if (TokenEncryption.isExpired(resetToken)) {
            // LOGGER.debug("User token expired: {}", userToken);
            throw ValidationExceptionBuilder.withErrorCode(ResetPasswordFailureReason.RESET_TOKEN_EXPIRED.name());
        }
    }

    private void setResetToken(User user) throws Exception {
        Date expireDate = DateUtil.addMinutesToDate(new Date(), 5);
        String encryptedToken = TokenEncryption.encrypt(TokenEncryption.DATE_FORMAT.format(expireDate) + TokenEncryption.CONCATENATE_SYMBOL + user.getId());
        user.setResetToken(encryptedToken);
        // LOGGER.info("Setting reset token for user with id : {} ", user);
        userRepository.save(user);
    }

    private User createUser(RegistrationRequest registrationRequest) throws Exception {
        User user = new User();
        String encryptedToken = TokenEncryption.formatAndEncrypt((new Date()));
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setStatus(UserStatus.UNCONFIRMED);
        user.setCreatedAt(new Date());
        user.setConfirmToken(encryptedToken);
        LOGGER.info("User is created in auth service");
        return userRepository.save(user);
    }
}
