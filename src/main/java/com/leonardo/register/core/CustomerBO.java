package com.leonardo.register.core;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@With
@Value
@FieldNameConstants
@Builder(toBuilder = true)
public class CustomerBO {

    Long id;
    String cpf;
    String name;
    String socialName;
    LocalDate birthDate;
    Address address;

    @With
    @Value
    @FieldNameConstants
    @Builder(toBuilder = true)
    public static class Address {

        String street;
        String number;
        String complement;
        String neighborhood;
        String city;
        String postalCode;

    }

}
