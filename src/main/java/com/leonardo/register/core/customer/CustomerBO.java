package com.leonardo.register.core.customer;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;

@With
@Value
@FieldNameConstants
@Builder(toBuilder = true)
public class CustomerBO {

    String id;
    String document;
    String name;
    String socialName;
    List<Phone> phones;
    LocalDate birthDate;
    Address address;

    @With
    @Value
    @FieldNameConstants
    @Builder(toBuilder = true)
    public static class Phone {

        String ddd;
        String number;
        PhoneType type;

    }

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

    @Data
    @FieldNameConstants
    @Builder(toBuilder = true)
    public static class Filter {

        private String document;
        private String nameOrSocialName;
        private String phoneNumber;
        private String address;
    }

}
