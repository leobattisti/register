package com.leonardo.register.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
public enum Errors {

    CUSTOMER_INVALID_DOCUMENT(1, UnprocessableContent.class, "The document <%s> is neither a CPF nor a CNPJ"),
    CUSTOMER_INVALID_BIRTH_DATE(2, UnprocessableContent.class, "Birth date <%s> is after the current date"),
    CUSTOMER_INVALID_PHONE_LENGTH(3, UnprocessableContent.class, "Phone number <%s> is invalid for type <%s>"),
    ;

    private final int errorCode;
    private final Class<? extends RegisterException> exception;
    private final String message;

    @SneakyThrows
    public RuntimeException formatException(Object... params) {
        return this.exception.getDeclaredConstructor(Integer.class, String.class)
                .newInstance(this.errorCode, String.format(this.message, params));
    }

}
