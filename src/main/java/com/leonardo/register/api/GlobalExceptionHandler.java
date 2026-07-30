package com.leonardo.register.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.xml.bind.ValidationException;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
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

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(
            String code,
            Set<String> messages
    ) {

    }

}
