package com.leonardo.register.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leonardo.register.core.exception.Errors;
import com.leonardo.register.core.exception.RegisterException;
import com.leonardo.register.core.exception.UnprocessableContent;
import jakarta.xml.bind.ValidationException;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String BAD_REQUEST = "400";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(it -> Objects.requireNonNullElse(it.getDefaultMessage(), StringUtils.EMPTY))
                .collect(Collectors.toSet());

        var errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST)
                .messages(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        var errorResponse = ErrorResponse.builder()
                .code(BAD_REQUEST)
                .messages(Set.of(ex.getMessage()))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ErrorResponse> handleRegisterException(RegisterException ex) {
        var responseError = ErrorResponse.builder()
                .code(ex.getFormattedCode())
                .messages(Set.of(ex.getMessage()))
                .build();

        for (Errors error : Errors.values()) {
            if (error.getErrorCode() == ex.getCode()) {
                var status = getHttpStatus(error);

                return new ResponseEntity<>(responseError, status);
            }
        }

        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus getHttpStatus(Errors error) {
        if (error.getException() == UnprocessableContent.class)
            return HttpStatus.UNPROCESSABLE_CONTENT;

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(
            String code,
            Set<String> messages
    ) {

    }

}
