package com.ubt.auth.service.refreshtoken;

import com.ubt.auth.service.authentication.AuthenticationResult;
import com.ubt.auth.service.authentication.AuthenticationSuccessReason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenSuccess extends AuthenticationResult<AuthenticationSuccessReason> {

    private final AuthenticationSuccessReason reason;
    private String authToken;
    private String refreshToken;

    public static RefreshTokenSuccess of(AuthenticationSuccessReason reason, String authToken, String refreshToken) {
        return new RefreshTokenSuccess(null, reason, authToken, refreshToken);
    }

    public RefreshTokenSuccess(String email, AuthenticationSuccessReason reason, String authToken, String refreshToken) {
        super(email);
        this.reason = reason;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public AuthenticationSuccessReason getReason() {
        return reason;
    }
}
