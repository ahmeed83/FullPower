package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;
import nl.rabobank.domain.authorizations.AuthorizedRequest;
import nl.rabobank.domain.customer.Customer;
import nl.rabobank.data.repository.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AuthorizationAPITest {

    private static final String POST_AUTHORIZE = "/rabobank/api/v1/authorize-account";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerRepository customerRepository;

    private Customer customer1;

    private Customer customer2;
    
    private Customer customer3;

    private AuthorizedRequest authorizedRequest;


    @BeforeEach
    void setUp() {
        customer1 = MockData.generateTestCustomer1();
        customer2 = MockData.generateTestCustomer2();
        customer3 = MockData.generateTestCustomer3();
    }
    
    @Test
    @DisplayName("It should assign account to a given customer successfully - Happy scenario")
    void ItShouldAssignAccountToGivenCustomerSuccessfully() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_SAVING_ACCOUNT)
                .authorizationType(AccountAuthorizationType.READ)
                .build();
        
        // When
        this.mockMvc.perform(post(POST_AUTHORIZE)
                                     .contentType(APPLICATION_JSON)
                                     .content(new ObjectMapper().writeValueAsString(authorizedRequest)))
                .andDo(print())
                // Then
                .andExpect(status().isAccepted());
    }
    
    @Test
    @DisplayName("It should not assign account to a given customer if the IBAN is already assigned")
    void ItShouldNotAssignAccountToGivenCustomerBecauseAccountIsAlreadyAssigned() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.READ)
                .build();
        // When
        this.mockMvc.perform(post(POST_AUTHORIZE)
                                     .contentType(APPLICATION_JSON)
                                     .content(new ObjectMapper().writeValueAsString(authorizedRequest)))
                .andDo(print())
                // Then
                .andExpect(jsonPath("$.errorMessage", Matchers.is("Grantee already has this IBAN with this Type. Please use different IBAN!")))
                .andExpect(status().isConflict());
    }
    
    @Test
    @DisplayName("It should assign account to a given customer with the same IBAN but different account type")
    void ItShouldAssignAccountToGivenCustomerIfDifferentTypeIsGiven() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.WRITE)
                .build();
        // When
        this.mockMvc.perform(post(POST_AUTHORIZE)
                                     .contentType(APPLICATION_JSON)
                                     .content(new ObjectMapper().writeValueAsString(authorizedRequest)))
                .andDo(print())
                // Then
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("It should not assign account to a given customer if the account type is not READ or WRITE")
    void ItShouldNotAssignAccountToGivenCustomerIfAccountTypeNotReadOrWrite() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_SAVING_ACCOUNT)
                .authorizationType(AccountAuthorizationType.OWNER)
                .build();

        // When
        this.mockMvc.perform(post(POST_AUTHORIZE)
                                     .contentType(APPLICATION_JSON)
                                     .content(new ObjectMapper().writeValueAsString(authorizedRequest)))
                .andDo(print())
                // Then
                .andExpect(jsonPath("$.errorMessage", Matchers.is("Please Use only READ or WRITE methods!")))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("It should not assign account to a given customer if the account does not belong to the grantor")
    void ItShouldNotAssignAccountToGivenCustomerIfCustomerDoesNotOwnsAccount() throws Exception {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        given(customerRepository.findById(MockData.CUSTOMER_3_ID)).willReturn(Optional.of(customer3));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_2_ID)
                .granteeId(MockData.CUSTOMER_3_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.WRITE)
                .build();
        // When
        this.mockMvc.perform(post(POST_AUTHORIZE)
                                     .contentType(APPLICATION_JSON)
                                     .content(new ObjectMapper().writeValueAsString(authorizedRequest)))
                .andDo(print())
                // Then
                .andExpect(jsonPath("$.errorMessage", Matchers.is("You dont have rights to assign this IBAN. Please use IBAN that belongs to you!")))
                .andExpect(status().isForbidden());
    }
}