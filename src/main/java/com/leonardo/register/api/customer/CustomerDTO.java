package com.leonardo.register.api.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

import java.time.LocalDate;

@With
@Builder(toBuilder = true)
public record CustomerDTO(

        Long id,
        @NotBlank(message = "CPF is required")
        String cpf,
        @NotBlank(message = "Name is required")
        String name,
        String socialName,
        @NotNull(message = "Birth date is required")
        LocalDate birthDate,
        @Valid
        @NotNull(message = "Address is required")
        Address address

) {

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
