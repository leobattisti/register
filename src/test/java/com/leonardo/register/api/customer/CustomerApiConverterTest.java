package com.leonardo.register.api.customer;

import com.leonardo.register.core.customer.CustomerBO;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomerApiConverterTest {

    @Spy
    private CustomerApiConverter converter = Mappers.getMapper(CustomerApiConverter.class);

    @Test
    void shouldConvertBoToResponse() {
        var dto = Instancio.create(CustomerDTO.class);

        var bo = converter.toBusiness(dto);

        assertThat(bo.getId()).isEqualTo(dto.id());
        assertThat(bo.getDocument()).isEqualTo(dto.document());
        assertThat(bo.getName()).isEqualTo(dto.name());
        assertThat(bo.getSocialName()).isEqualTo(dto.socialName());
        assertThat(bo.getPhones().getFirst().getDdd()).isEqualTo(dto.phones().getFirst().ddd());
        assertThat(bo.getPhones().getFirst().getNumber()).isEqualTo(dto.phones().getFirst().number());
        assertThat(bo.getPhones().getFirst().getType()).isEqualTo(dto.phones().getFirst().type());
        assertThat(bo.getBirthDate()).isEqualTo(dto.birthDate());
        assertThat(bo.getAddress().getStreet()).isEqualTo(dto.address().street());
        assertThat(bo.getAddress().getNumber()).isEqualTo(dto.address().number());
        assertThat(bo.getAddress().getComplement()).isEqualTo(dto.address().complement());
        assertThat(bo.getAddress().getNeighborhood()).isEqualTo(dto.address().neighborhood());
        assertThat(bo.getAddress().getCity()).isEqualTo(dto.address().city());
        assertThat(bo.getAddress().getPostalCode()).isEqualTo(dto.address().postalCode());
    }

    @Test
    void shouldConvertDtoToBo() {
        var bo = Instancio.create(CustomerBO.class);

        var dto = converter.toResponse(bo);

        assertThat(dto.id()).isEqualTo(bo.getId());
        assertThat(dto.document()).isEqualTo(bo.getDocument());
        assertThat(dto.name()).isEqualTo(bo.getName());
        assertThat(dto.socialName()).isEqualTo(bo.getSocialName());
        assertThat(dto.birthDate()).isEqualTo(bo.getBirthDate());
        assertThat(dto.phones().getFirst().ddd()).isEqualTo(bo.getPhones().getFirst().getDdd());
        assertThat(dto.phones().getFirst().number()).isEqualTo(bo.getPhones().getFirst().getNumber());
        assertThat(dto.phones().getFirst().type()).isEqualTo(bo.getPhones().getFirst().getType());
        assertThat(dto.address().street()).isEqualTo(bo.getAddress().getStreet());
        assertThat(dto.address().number()).isEqualTo(bo.getAddress().getNumber());
        assertThat(dto.address().complement()).isEqualTo(bo.getAddress().getComplement());
        assertThat(dto.address().neighborhood()).isEqualTo(bo.getAddress().getNeighborhood());
        assertThat(dto.address().city()).isEqualTo(bo.getAddress().getCity());
        assertThat(dto.address().postalCode()).isEqualTo(bo.getAddress().getPostalCode());
    }

}
