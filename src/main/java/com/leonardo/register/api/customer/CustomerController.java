package com.leonardo.register.api.customer;

import com.leonardo.register.core.customer.CustomerBO;
import com.leonardo.register.core.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customers")
public class CustomerController {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final String DEFAULT_SORT = "createdAt";

    private final CustomerApiConverter converter;
    private final CustomerService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find customer by id")
    public CustomerDTO findById(@PathVariable String id) {
        return converter.toResponse(service.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find customer by filters")
    public Page<CustomerDTO> findByFilter(@RequestParam(required = false) String document,
                                          @RequestParam(required = false) String nameOrSocialName,
                                          @RequestParam(required = false) String phoneNumber,
                                          @RequestParam(required = false) String address,
                                          @ParameterObject
                                          @PageableDefault(
                                                  size = DEFAULT_PAGE_SIZE,
                                                  sort = DEFAULT_SORT,
                                                  direction = Sort.Direction.DESC) Pageable pageable) {
        var filter = CustomerBO.Filter.builder()
                .document(document)
                .nameOrSocialName(nameOrSocialName)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        return service.findByFilter(filter, pageable)
                .map(converter::toResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create valid customer")
    public CustomerDTO create(@RequestBody @Valid CustomerDTO dto) {
        var bo = converter.toBusiness(dto);

        return converter.toResponse(service.create(bo));
    }

}
