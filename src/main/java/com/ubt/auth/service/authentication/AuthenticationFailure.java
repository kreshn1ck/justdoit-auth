package com.ubt.auth.service.authentication;

public class AuthenticationFailure extends AuthenticationResult<AuthenticationFailureReason> {

    private final AuthenticationFailureReason reason;

    public AuthenticationFailure(String email, AuthenticationFailureReason reason) {
        super(email);
        this.reason = reason;
    }

    public static AuthenticationFailure of(AuthenticationFailureReason reason) {
        return new AuthenticationFailure(null, reason);
    }

    public static AuthenticationFailure of(String email, AuthenticationFailureReason reason) {
        return new AuthenticationFailure(email, reason);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public AuthenticationFailureReason getReason() {
        return reason;
    }
}