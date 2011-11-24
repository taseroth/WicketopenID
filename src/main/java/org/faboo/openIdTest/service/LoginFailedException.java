package org.faboo.openIdTest.service;

/**
 */
public class LoginFailedException extends Exception {

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }
}
