package com.ubt.auth.service.refreshtoken;

import com.ubt.auth.exception.ValidationExceptionBuilder;
import com.ubt.auth.model.RefreshToken;
import com.ubt.auth.model.User;
import com.ubt.auth.repository.RefreshTokenRepository;
import com.ubt.auth.service.authentication.AuthenticationFailure;
import com.ubt.auth.service.authentication.AuthenticationFailureReason;
import com.ubt.auth.service.authentication.AuthenticationSuccessReason;
import com.ubt.auth.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

     private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24);
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), user, calendar.getTime());
        refreshTokenRepository.save(refreshToken);
        // LOGGER.info("Create refresh token successfully");
        return refreshToken.getToken();
    }

    public RefreshTokenSuccess renewAuthenticationFromRefreshToken(String token) {
         RefreshToken refreshToken = refreshTokenRepository.getRefreshTokenByToken(token);
         validateRefreshToken(refreshToken);
        return getRefreshTokenResponse(refreshToken);
    }

    private void validateRefreshToken(RefreshToken refreshToken) {
        Optional<AuthenticationFailure> authenticationError = Optional.empty();

        if (refreshToken == null) {
            authenticationError = Optional.of(AuthenticationFailure.of(AuthenticationFailureReason.BAD_REFRESH_TOKEN));
        }
        else if (refreshTokenIsExpired(refreshToken)) {
            authenticationError = Optional.of(AuthenticationFailure.of(AuthenticationFailureReason.REFRESH_TOKEN_EXPIRED));
        }

        if (authenticationError.isPresent()) {
            throw ValidationExceptionBuilder.withErrorCode(authenticationError.get().getReason().name());
        }
    }

    private boolean refreshTokenIsExpired(RefreshToken refreshToken) {
        // LOGGER.info("Checking for refresh token: {} expiration", refreshToken);
        return new Date().after(refreshToken.getExpireDate());
    }

    private RefreshTokenSuccess getRefreshTokenResponse(RefreshToken refreshToken) {
        User user = refreshToken.getUser();
        String newAuthToken = JwtUtil.generateToken(user);
        String newRefreshToken = createRefreshToken(user);
        refreshTokenRepository.delete(refreshToken);
        // LOGGER.info("User with id: {} token refreshed", user.getId());
        return RefreshTokenSuccess.of(AuthenticationSuccessReason.REFRESHED_TOKEN, newAuthToken, newRefreshToken);
    }
}
