package com.leonardo.register.api.customer;

import com.leonardo.register.api.GlobalExceptionHandler;
import com.leonardo.register.core.customer.CustomerService;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@ComponentScan(basePackageClasses = CustomerApiConverter.class)
class CustomerControllerTest {

    private static final String URL = "/customers";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String BAD_REQUEST_CODE = "400";

    private static final String[] ERROR_MESSAGES_CUSTOMER = {"Document is required",
            "Name is required",
            "Phone is required",
            "Birth date is required",
            "Address is required"};
    private static final String[] ERROR_MESSAGES_PHONE = {"Phone DDD is required",
            "Phone number is required",
            "Phone type is required"};
    private static final String[] ERROR_MESSAGES_ADDRESS = {"Street is required",
            "Neighborhood is required",
            "City is required",
            "Postal code is required"};

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CustomerService service;

    @Spy
    private CustomerApiConverter converter = Mappers.getMapper(CustomerApiConverter.class);

    @Test
    @SneakyThrows
    void shouldCreateCustomer() {
        var request = Instancio.create(CustomerDTO.class);
        var content = OBJECT_MAPPER.writeValueAsString(request);

        var bo = converter.toBusiness(request);

        when(service.create(bo)).thenReturn(bo);

        mockMvc.perform(post(URL).content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bo.getId().toString()))
                .andExpect(jsonPath("$.document").value(bo.getDocument()))
                .andExpect(jsonPath("$.name").value(bo.getName()))
                .andExpect(jsonPath("$.socialName").value(bo.getSocialName()))
                .andExpect(jsonPath("$.phones[0].ddd").value(bo.getPhones().getFirst().getDdd()))
                .andExpect(jsonPath("$.phones[0].number").value(bo.getPhones().getFirst().getNumber()))
                .andExpect(jsonPath("$.phones[0].type").value(bo.getPhones().getFirst().getType().toString()))
                .andExpect(jsonPath("$.birthDate").value(bo.getBirthDate().toString()))
                .andExpect(jsonPath("$.address.street").value(bo.getAddress().getStreet()))
                .andExpect(jsonPath("$.address.number").value(bo.getAddress().getNumber()))
                .andExpect(jsonPath("$.address.complement").value(bo.getAddress().getComplement()))
                .andExpect(jsonPath("$.address.neighborhood").value(bo.getAddress().getNeighborhood()))
                .andExpect(jsonPath("$.address.city").value(bo.getAddress().getCity()))
                .andExpect(jsonPath("$.address.postalCode").value(bo.getAddress().getPostalCode()));

        verify(service).create(bo);
    }

    @Test
    @SneakyThrows
    void shouldNotCreateCustomerWhenCustomerDtoIsEmpty() {
        var request = CustomerDTO.builder().build();
        var content = OBJECT_MAPPER.writeValueAsString(request);

        var response = mockMvc.perform(post(URL).content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorResponse = OBJECT_MAPPER.readValue(response.getResponse().getContentAsString(), GlobalExceptionHandler.ErrorResponse.class);

        assertThat(errorResponse.code()).isEqualTo(BAD_REQUEST_CODE);
        assertThat(errorResponse.messages()).containsExactlyInAnyOrder(ERROR_MESSAGES_CUSTOMER);

        verifyNoInteractions(service);
    }

    @Test
    @SneakyThrows
    void shouldNotCreateCustomerWhenAddressCustomerDtoIsEmpty() {
        var request = Instancio.create(CustomerDTO.class);
        var addressEmpty = CustomerDTO.Address.builder().build();

        var content = OBJECT_MAPPER.writeValueAsString(request.withAddress(addressEmpty));

        var response = mockMvc.perform(post(URL).content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorResponse = OBJECT_MAPPER.readValue(response.getResponse().getContentAsString(), GlobalExceptionHandler.ErrorResponse.class);

        assertThat(errorResponse.code()).isEqualTo(BAD_REQUEST_CODE);
        assertThat(errorResponse.messages()).containsExactlyInAnyOrder(ERROR_MESSAGES_ADDRESS);

        verifyNoInteractions(service);
    }

    @Test
    @SneakyThrows
    void shouldNotCreateCustomerWhenPhoneCustomerDtoIsEmpty() {
        var request = Instancio.create(CustomerDTO.class);
        var phone = CustomerDTO.Phone.builder().build();

        var content = OBJECT_MAPPER.writeValueAsString(request.withPhones(List.of(phone)));

        var response = mockMvc.perform(post(URL).content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorResponse = OBJECT_MAPPER.readValue(response.getResponse().getContentAsString(), GlobalExceptionHandler.ErrorResponse.class);

        assertThat(errorResponse.code()).isEqualTo(BAD_REQUEST_CODE);
        assertThat(errorResponse.messages()).containsExactlyInAnyOrder(ERROR_MESSAGES_PHONE);

        verifyNoInteractions(service);
    }
}
