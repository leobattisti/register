package com.leonardo.register.core.customer;

import com.leonardo.register.core.exception.Errors;
import com.leonardo.register.data.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final int CPF_LENGTH = 11;
    private static final int CNPJ_LENGTH = 14;

    private static final String MOBILE_NUMBER_REGEX = "^\\d{9}$";
    private static final String LANDLINE_NUMBER_REGEX = "^\\d{8}$";

    private final Clock clock;
    private final CustomerRepository repository;

    @Override
    public CustomerBO findById(String id) {
        return repository.findById(id);
    }

    @Override
    public CustomerBO create(CustomerBO bo) {
        documentValidator(bo.getDocument());
        phoneValidator(bo.getPhones());
        birthDateValidator(bo);

        log.info("Creating customer with document <{}>", bo.getDocument());

        return repository.save(bo);
    }

    private void documentValidator(String document) {
        if (isFalse(isValidDocumentLength(document))) {
            log.error("Document <%s> has invalid length");
            throw Errors.CUSTOMER_INVALID_DOCUMENT.formatException(document);
        }
    }

    public void phoneValidator(List<CustomerBO.Phone> phones) {
        var invalidPhone = phones.stream()
                .filter(this::isInvalidPhoneNumber)
                .findFirst();

        if (invalidPhone.isPresent()) {
            var phoneNumber = invalidPhone.get().getNumber();
            var phoneType = invalidPhone.get().getType();

            log.error("Phone number <{}> has invalid length for type <{}>", phoneNumber, phoneType);
            throw Errors.CUSTOMER_INVALID_PHONE_LENGTH.formatException(phoneNumber, phoneType);
        }
    }

    private void birthDateValidator(CustomerBO bo) {
        var today = LocalDate.now(clock);
        var birthDate = bo.getBirthDate();

        if (birthDate.isAfter(today)) {
            log.error("Birth date <{}> is after the current date for customer <{}>", birthDate, bo.getDocument());
            throw Errors.CUSTOMER_INVALID_BIRTH_DATE.formatException(birthDate);
        }
    }

    private boolean isValidDocumentLength(String document) {
        return document.length() == CPF_LENGTH || document.length() == CNPJ_LENGTH;
    }

    private boolean isInvalidPhoneNumber(CustomerBO.Phone phone) {
        var phoneRegex = phone.getType() == PhoneType.MOBILE ? MOBILE_NUMBER_REGEX : LANDLINE_NUMBER_REGEX;

        return !Pattern.compile(phoneRegex)
                .matcher(phone.getNumber())
                .matches();
    }

}
