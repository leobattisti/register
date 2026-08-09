package com.leonardo.register.api.customer;

import com.leonardo.register.api.GlobalExceptionHandler;
import com.leonardo.register.core.customer.CustomerBO;
import com.leonardo.register.core.customer.CustomerService;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockitoSpyBean
    private final CustomerApiConverter converter = Mappers.getMapper(CustomerApiConverter.class);

    @Captor
    private final ArgumentCaptor<CustomerBO.Filter> filterArgumentCaptor = ArgumentCaptor.forClass(CustomerBO.Filter.class);

    @Captor
    private final ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

    @Test
    @SneakyThrows
    void shouldFindCustomerById() {
        var id = Instancio.create(String.class);
        var bo = Instancio.create(CustomerBO.class);
        var urlFindById = URL + "/" + id;

        when(service.findById(id)).thenReturn(bo);

        mockMvc.perform(get(urlFindById))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bo.getId()))
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

        verify(service).findById(id);
    }

    @Test
    @SneakyThrows
    void shouldFindCustomerByFilterWithDefaultPagination() {
        var bo = Instancio.create(CustomerBO.class);
        var page = new PageImpl<>(List.of(bo));

        when(service.findByFilter(any(), any()))
                .thenReturn(page);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(bo.getId()))
                .andExpect(jsonPath("$.content[0].document").value(bo.getDocument()))
                .andExpect(jsonPath("$.content[0].name").value(bo.getName()))
                .andExpect(jsonPath("$.content[0].socialName").value(bo.getSocialName()))
                .andExpect(jsonPath("$.content[0].phones[0].ddd").value(bo.getPhones().getFirst().getDdd()))
                .andExpect(jsonPath("$.content[0].phones[0].number").value(bo.getPhones().getFirst().getNumber()))
                .andExpect(jsonPath("$.content[0].phones[0].type").value(bo.getPhones().getFirst().getType().toString()))
                .andExpect(jsonPath("$.content[0].birthDate").value(bo.getBirthDate().toString()))
                .andExpect(jsonPath("$.content[0].address.street").value(bo.getAddress().getStreet()))
                .andExpect(jsonPath("$.content[0].address.number").value(bo.getAddress().getNumber()))
                .andExpect(jsonPath("$.content[0].address.complement").value(bo.getAddress().getComplement()))
                .andExpect(jsonPath("$.content[0].address.neighborhood").value(bo.getAddress().getNeighborhood()))
                .andExpect(jsonPath("$.content[0].address.city").value(bo.getAddress().getCity()))
                .andExpect(jsonPath("$.content[0].address.postalCode").value(bo.getAddress().getPostalCode()));

        verify(service).findByFilter(filterArgumentCaptor.capture(), pageableArgumentCaptor.capture());
        verify(converter).toResponse(bo);

        var filter = filterArgumentCaptor.getValue();
        var pageable = pageableArgumentCaptor.getValue();

        assertThat(filter).hasAllNullFieldsOrProperties();

        assertThat(pageable)
                .satisfies(it -> {

                    assertThat(it.getPageSize()).isEqualTo(50);
                    assertThat(it.getPageNumber()).isEqualTo(0);
                    assertThat(it.getSort().get()).contains(Sort.Order.desc("createdAt"));
                });
    }

    @Test
    @SneakyThrows
    void shouldFindCustomerByFilterWithFilterParams() {
        var bo = Instancio.create(CustomerBO.class);
        var page = new PageImpl<>(List.of(bo));

        var document = Instancio.create(String.class);
        var nameOrSocialName = Instancio.create(String.class);
        var phoneNumber = Instancio.create(String.class);
        var address = Instancio.create(String.class);

        when(service.findByFilter(any(), any()))
                .thenReturn(page);

        mockMvc.perform(get(URL)
                        .queryParam(CustomerBO.Filter.Fields.document, document)
                        .queryParam(CustomerBO.Filter.Fields.nameOrSocialName, nameOrSocialName)
                        .queryParam(CustomerBO.Filter.Fields.phoneNumber, phoneNumber)
                        .queryParam(CustomerBO.Filter.Fields.address, address)
                        .queryParam("page", "2")
                        .queryParam("size", "20")
                        .queryParam("sort", "createdAt,asc")
                        .queryParam("sort", "document,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(bo.getId()))
                .andExpect(jsonPath("$.content[0].document").value(bo.getDocument()))
                .andExpect(jsonPath("$.content[0].name").value(bo.getName()))
                .andExpect(jsonPath("$.content[0].socialName").value(bo.getSocialName()))
                .andExpect(jsonPath("$.content[0].phones[0].ddd").value(bo.getPhones().getFirst().getDdd()))
                .andExpect(jsonPath("$.content[0].phones[0].number").value(bo.getPhones().getFirst().getNumber()))
                .andExpect(jsonPath("$.content[0].phones[0].type").value(bo.getPhones().getFirst().getType().toString()))
                .andExpect(jsonPath("$.content[0].birthDate").value(bo.getBirthDate().toString()))
                .andExpect(jsonPath("$.content[0].address.street").value(bo.getAddress().getStreet()))
                .andExpect(jsonPath("$.content[0].address.number").value(bo.getAddress().getNumber()))
                .andExpect(jsonPath("$.content[0].address.complement").value(bo.getAddress().getComplement()))
                .andExpect(jsonPath("$.content[0].address.neighborhood").value(bo.getAddress().getNeighborhood()))
                .andExpect(jsonPath("$.content[0].address.city").value(bo.getAddress().getCity()))
                .andExpect(jsonPath("$.content[0].address.postalCode").value(bo.getAddress().getPostalCode()));

        verify(service).findByFilter(filterArgumentCaptor.capture(), pageableArgumentCaptor.capture());
        verify(converter).toResponse(bo);

        var filter = filterArgumentCaptor.getValue();
        var pageable = pageableArgumentCaptor.getValue();

        assertThat(filter)
                .satisfies(it -> {

                    assertThat(it.getDocument()).isEqualTo(document);
                    assertThat(it.getNameOrSocialName()).isEqualTo(nameOrSocialName);
                    assertThat(it.getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(it.getAddress()).isEqualTo(address);
                });

        assertThat(pageable)
                .satisfies(it -> {

                    assertThat(it.getPageSize()).isEqualTo(20);
                    assertThat(it.getPageNumber()).isEqualTo(2);
                    assertThat(it.getSort().get())
                            .containsExactly(Sort.Order.asc("createdAt"), Sort.Order.desc("document"));
                });
    }

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
                .andExpect(jsonPath("$.id").value(bo.getId()))
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
