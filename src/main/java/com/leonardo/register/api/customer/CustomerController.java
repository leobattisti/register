package com.leonardo.register.api.customer;

import com.leonardo.register.core.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerApiConverter converter;
    private final CustomerService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create valid customer")
    public CustomerDTO create(@RequestBody @Valid CustomerDTO dto) {
        var bo = converter.toBusiness(dto);

        return converter.toResponse(service.create(bo));
    }

}
