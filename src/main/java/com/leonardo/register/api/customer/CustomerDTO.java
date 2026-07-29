package com.leonardo.register.api.customer;

import lombok.Builder;
import lombok.With;

import java.time.LocalDate;

@With
@Builder(toBuilder = true)
public record CustomerDTO(

        Long id,
        String cpf,
        String name,
        String socialName,
        LocalDate birthDate,
        Address address

) {

    public record Address (

            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String postalCode

    ) {

    }
}
