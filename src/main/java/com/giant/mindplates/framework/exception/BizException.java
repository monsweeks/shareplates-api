package com.giant.mindplates.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BizException extends RuntimeException {
    public BizException() {

    }

    public BizException(String message) {
        super(message);
    }
}


