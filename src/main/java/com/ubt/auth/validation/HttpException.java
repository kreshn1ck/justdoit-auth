package com.ubt.auth.validation;

public class HttpException extends RuntimeException {

    public static final int INTERNAL_SERVER_ERROR = 500;

    protected final int status;

    private final String errorCode;

    public HttpException() {
        this(INTERNAL_SERVER_ERROR);
    }

    public HttpException(int status) {
        this(status, "", "");
    }

    public HttpException(int status, String message) {
        this(status, message, "");
    }

    public HttpException(int status, Throwable cause) {
        this(status, cause.getMessage(), "", cause);
    }

    public HttpException(String message) {
        this(INTERNAL_SERVER_ERROR, message, "");
    }

    public HttpException(String message, String errorCode) {
        this(INTERNAL_SERVER_ERROR, message, errorCode);
    }

    public HttpException(String message, Throwable cause) {
        this(INTERNAL_SERVER_ERROR, message, "", cause);
    }

    public HttpException(String message, String errorCode, Throwable cause) {
        this(INTERNAL_SERVER_ERROR, message, errorCode, cause);
    }

    public HttpException(Throwable cause) {
        this(INTERNAL_SERVER_ERROR, cause.getMessage(), "", cause);
    }

    public HttpException(int status, String message, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpException(int status, String message, Throwable cause) {
        this(status, message, "", cause);
    }

    public HttpException(int status, String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }
}

