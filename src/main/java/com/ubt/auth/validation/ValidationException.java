package com.ubt.auth.validation;

import java.util.ArrayList;
import java.util.List;

@TypescriptClass
public class ValidationException extends HttpException {

    public static final int BAD_REQUEST = 400;

    private final transient List<ValidationError> errors = new ArrayList<>();

    public ValidationException() {
        super(BAD_REQUEST, "There were validation errors", "validation");
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public ValidationException add(ValidationError error) {
        errors.add(error);
        return this;
    }

    public ValidationException add(String attribute, String errorCode) {
        return add(attribute, "", errorCode);
    }

    public ValidationException add(String attribute, String error, String errorCode) {
        ValidationError validationError = new ValidationError(attribute, error, errorCode);
        return add(validationError);
    }

    public ValidationException addAll(List<ValidationError> errors) {
        this.errors.addAll(errors);
        return this;
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }

}
