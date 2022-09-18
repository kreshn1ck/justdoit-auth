package com.ubt.auth.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotate model classes with this annotation to generate API classes for them even
 * though they are not mentioned in controllers.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TypescriptClass {
}