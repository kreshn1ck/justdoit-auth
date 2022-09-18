package com.ubt.auth.service.authentication;

public abstract class AuthenticationResult<E extends Enum<E>> {

    private final String email;

    public AuthenticationResult(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public abstract boolean isSuccess();

    public abstract E getReason();
}