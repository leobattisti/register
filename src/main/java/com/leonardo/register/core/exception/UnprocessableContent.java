package com.leonardo.register.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnprocessableContent extends RegisterException {

    public UnprocessableContent(Integer code, String message) {
        super(code, message);
    }

}
