package com.leonardo.register.api.customer;

import com.leonardo.register.core.customer.PhoneType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

import java.time.LocalDate;
import java.util.List;

@With
@Builder(toBuilder = true)
public record CustomerDTO(

        String id,
        @NotBlank(message = "Document is required")
        String document,
        @NotBlank(message = "Name is required")
        String name,
        String socialName,
        @Valid
        @NotEmpty(message = "Phone is required")
        List<Phone> phones,
        @NotNull(message = "Birth date is required")
        LocalDate birthDate,
        @Valid
        @NotNull(message = "Address is required")
        Address address

) {

    @With
    @Builder(toBuilder = true)
    public record Phone(

            @NotBlank(message = "Phone DDD is required")
            String ddd,
            @NotBlank(message = "Phone number is required")
            String number,
            @NotNull(message = "Phone type is required")
            PhoneType type
    ) {

    }

    @With
    @Builder(toBuilder = true)
    public record Address(

            @NotBlank(message = "Street is required")
            String street,
            String number,
            String complement,
            @NotBlank(message = "Neighborhood is required")
            String neighborhood,
            @NotBlank(message = "City is required")
            String city,
            @NotBlank(message = "Postal code is required")
            String postalCode
    ) {

    }

}
