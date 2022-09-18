package com.ubt.auth.exception;

import com.ubt.auth.validation.ValidationError;
import com.ubt.auth.validation.ValidationException;

public class ValidationExceptionBuilder {

    private ValidationException validationException;

    public ValidationExceptionBuilder() {
    }

    public static ValidationException withErrorCode(String errorCode) {
        ValidationException exception = new ValidationException();
        ValidationError error = new ValidationError("", "", errorCode);
        exception.add(error);
        return exception;
    }

    public static ValidationException withErrorCodeAndAttribute(String errorCode, String attribute) {
        ValidationException exception = new ValidationException();
        ValidationError error = new ValidationError(attribute, "", errorCode);
        exception.add(error);
        return exception;
    }

    public static ValidationException withErrorCodeAndAttributeAndError(String errorCode, String attribute, String error) {
        ValidationException exception = new ValidationException();
        ValidationError validationError = new ValidationError(attribute, error, errorCode);
        exception.add(validationError);
        return exception;
    }
}