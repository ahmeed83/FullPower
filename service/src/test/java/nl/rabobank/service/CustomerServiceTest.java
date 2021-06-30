package nl.rabobank.service;

import nl.rabobank.data.repository.CustomerRepository;
import nl.rabobank.domain.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer1;

    private Customer customer2;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
        customer1 = MockData.generateTestCustomer1();
        customer2 = MockData.generateTestCustomer2();
    }
    
    @Test
    @DisplayName("It should return the accounts belongs to the customer grantor")
    void itShouldReturnCustomerDetailsBelongsToCustomerGrantor() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        // When
        final var customerDetails = customerService.getCustomerAccountDetails(MockData.CUSTOMER_1_ID);
        // Then
        assertThat(customerDetails.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("It should return the accounts belongs to the customer grantee")
    void itShouldReturnCustomerDetailsBelongsToCustomerGrantee() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        // When
        final var customerDetails = customerService.getCustomerAccountDetails(MockData.CUSTOMER_2_ID);
        // Then
        assertThat(customerDetails.size()).isEqualTo(2);
    }
}