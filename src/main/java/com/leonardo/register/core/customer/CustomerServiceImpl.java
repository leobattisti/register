package com.leonardo.register.core.customer;

import com.leonardo.register.core.exception.Errors;
import com.leonardo.register.data.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final int CPF_LENGTH = 11;
    private static final int CNPJ_LENGTH = 14;

    private final Clock clock;
    private final CustomerRepository repository;

    @Override
    public CustomerBO findById(String id) {
        return repository.findById(id);
    }

    @Override
    public CustomerBO create(CustomerBO bo) {
        documentValidator(bo.getDocument());
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

}
