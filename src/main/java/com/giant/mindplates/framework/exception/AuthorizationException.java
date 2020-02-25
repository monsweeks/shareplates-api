package com.giant.mindplates.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {

    }

    public AuthorizationException(String message) {
        super(message);
    }
}


