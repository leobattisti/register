package com.leonardo.register.core.customer;

import com.leonardo.register.core.exception.RegisterException;
import com.leonardo.register.data.customer.CustomerRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private static final ZoneId AMERICAS_SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final String INSTANT_DATE = "2026-08-02T14:30:00Z";

    @InjectMocks
    private CustomerServiceImpl service;

    @Mock
    Clock clock;

    @Mock
    private CustomerRepository repository;

    @ParameterizedTest
    @ValueSource(strings = {"12345678912", "12345678912345"})
    void shouldSaveCustomer(String document) {
        var birthDate = LocalDate.of(2026, 8, 2);
        var customer = Instancio.create(CustomerBO.class)
                .withDocument(document)
                .withBirthDate(birthDate);

        when(clock.instant()).thenReturn(Instant.parse(INSTANT_DATE));
        when(clock.getZone()).thenReturn(AMERICAS_SAO_PAULO_ZONE);
        when(repository.save(customer)).thenReturn(customer);

        var result = service.create(customer);

        assertThat(result).isEqualTo(customer);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567891", "123456789123", "1234567891234", "123456789123456"})
    void shouldNotSaveCustomerWhenDocumentHasInvalidLength(String document) {
        var customer = Instancio.create(CustomerBO.class)
                .withDocument(document);
        var errorMessage = String.format("The document <%s> is neither a CPF nor a CNPJ", customer.getDocument());

        assertThatThrownBy(() -> service.create(customer))
                .isInstanceOf(RegisterException.class)
                .hasMessage(errorMessage);

        verifyNoInteractions(repository, clock);
    }

    @Test
    void shouldNotSaveCustomerWhenBirthDateIsInvalid() {
        var document = "99988866621";
        var birthDate = LocalDate.of(2026, 8, 3);
        var customer = Instancio.create(CustomerBO.class)
                .withDocument(document)
                .withBirthDate(birthDate);
        var errorMessage = String.format("Birth date <%s> is after the current date", birthDate);

        when(clock.instant()).thenReturn(Instant.parse(INSTANT_DATE));
        when(clock.getZone()).thenReturn(AMERICAS_SAO_PAULO_ZONE);

        assertThatThrownBy(() -> service.create(customer))
                .isInstanceOf(RegisterException.class)
                .hasMessage(errorMessage);

        verifyNoInteractions(repository);
    }

}
