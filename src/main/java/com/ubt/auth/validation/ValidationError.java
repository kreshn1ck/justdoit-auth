package com.ubt.auth.validation;

/**
 * Represents a validation error attached to a {@link ValidationException}.
 */
@TypescriptClass
public class ValidationError {

    private final String attribute;
    private final String error;
    private final String errorCode;
    private final String additionalInfo;

    public ValidationError(String attribute, String error, String errorCode) {
        this(attribute, error, errorCode, "");
    }

    public ValidationError(String attribute, String error, String errorCode, String additionalInfo) {
        this.attribute = attribute;
        this.error = error;
        this.errorCode = errorCode;
        this.additionalInfo = additionalInfo;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getError() {
        return error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
