package com.ubt.auth.service.user;

public enum ResetPasswordFailureReason {
    INVALID_RESET_TOKEN,
    INVALID_EMAIL,
    RESET_TOKEN_EXPIRED,
    CONFIRM_PASSWORD_DOES_NOT_MATCH
}
