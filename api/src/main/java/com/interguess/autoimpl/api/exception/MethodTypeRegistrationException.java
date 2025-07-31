package com.interguess.autoimpl.api.exception;

import lombok.experimental.StandardException;

/**
 * Exception thrown when there is an error during the registration of a method type.
 * This could occur if the method type is already registered or if there is a conflict
 * with an existing method type.
 */
@StandardException
public class MethodTypeRegistrationException extends RuntimeException {
}
