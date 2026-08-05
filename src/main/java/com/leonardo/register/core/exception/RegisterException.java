package com.leonardo.register.core.exception;

import lombok.Getter;

@Getter
public class RegisterException extends RuntimeException {

    private static final String PREFIX = "REGISTER-";

    private final Integer code;
    private final String message;

    public RegisterException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getFormattedCode() {
        return PREFIX + this.code;
    }

}
