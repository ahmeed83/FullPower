package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.domain.customer.CustomerAccountDetails;
import nl.rabobank.domain.customer.Customer;
import nl.rabobank.data.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CustomerAPITest {

    private static final String GET_CUSTOMER_DETAILS = "/rabobank/api/v1/customer-account-details/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerRepository customerRepository;

    private Customer customer1;
    
    private Customer customer2;
    
    @BeforeEach
    void setUp() {
        customer1 = MockData.generateTestCustomer1();
        customer2 = MockData.generateTestCustomer2();
    }
    
    @Test
    @DisplayName("It should return the accounts belongs to the customer grantor")
    void itShouldReturnCustomerDetailsBelongsToCustomerGrantor() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        List<CustomerAccountDetails> customerDetails = customer1.getCustomerDetails();
         
        // When
        this.mockMvc.perform(get(GET_CUSTOMER_DETAILS + MockData.CUSTOMER_1_ID))
                .andDo(print())
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.*.*", hasSize(4)))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(customerDetails)));
    }

    @Test
    @DisplayName("It should return the accounts belongs to the customer grantee")
    void itShouldReturnCustomerDetailsBelongsToCustomerGrantee() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        List<CustomerAccountDetails> customerDetails = customer2.getCustomerDetails();

        // When
        this.mockMvc.perform(get(GET_CUSTOMER_DETAILS + MockData.CUSTOMER_2_ID))
                .andDo(print())
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.*.*", hasSize(4)))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(customerDetails)));
    }
}