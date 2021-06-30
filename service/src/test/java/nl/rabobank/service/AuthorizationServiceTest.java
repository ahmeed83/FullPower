package nl.rabobank.service;

import nl.rabobank.data.repository.AccountRepository;
import nl.rabobank.data.repository.CustomerRepository;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;
import nl.rabobank.domain.authorizations.AuthorizedRequest;
import nl.rabobank.domain.customer.Customer;
import nl.rabobank.exception.business.AuthorizationMethodNotAcceptedException;
import nl.rabobank.exception.business.GranteeHasIBANAlreadyException;
import nl.rabobank.exception.business.IbanDoesNotBelongsToGrantorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    private AuthorizedRequest authorizedRequest;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer1;

    private Customer customer2;

    private Customer customer3;
    
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        authorizationService = new AuthorizationService(accountRepository, customerRepository);
        customer1 = MockData.generateTestCustomer1();
        customer2 = MockData.generateTestCustomer2();
        customer3 = MockData.generateTestCustomer3();
    }

    @Test
    @DisplayName("Happy scenario - It should return accounts that belongs to customer - Happy scenario")
    void ItShouldAssignAccountToGivenCustomerSuccessfully() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        given(accountRepository.findAccountByAccountNumber(MockData.CUSTOMER_1_SAVING_ACCOUNT)).willReturn(
                Optional.of(MockData.customer_1_SavingAccount));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_SAVING_ACCOUNT)
                .authorizationType(AccountAuthorizationType.READ)
                .build();
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
        // When
        authorizationService.authorizeAccountToCustomer(authorizedRequest);
        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(customer2);
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("It should not assign account to a given customer if the IBAN is already assigned")
    void ItShouldNotAssignAccountToGivenCustomerBecauseAccountIsAlreadyAssigned() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.READ)
                .build();
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
        // When
        // Then
        assertThatExceptionOfType(GranteeHasIBANAlreadyException.class).isThrownBy(
                () -> authorizationService.authorizeAccountToCustomer(authorizedRequest))
                .withMessage("Grantee already has this IBAN with this Type. Please use different IBAN!");
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("It should assign account to a given customer with the same IBAN but different account type")
    void ItShouldAssignAccountToGivenCustomerIfDifferentTypeIsGiven() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        given(accountRepository.findAccountByAccountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)).willReturn(
                Optional.of(MockData.customer_1_PaymentAccount));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.WRITE)
                .build();
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
        // When
        authorizationService.authorizeAccountToCustomer(authorizedRequest);
        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        assertThat(customerArgumentCaptor.getValue()).isEqualTo(customer2);
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("It should not assign account to a given customer if the account type is not READ or WRITE")
    void ItShouldNotAssignAccountToGivenCustomerIfAccountTypeNotReadOrWrite() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_1_ID)).willReturn(Optional.of(customer1));
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_1_ID)
                .granteeId(MockData.CUSTOMER_2_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.OWNER)
                .build();
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
        // When
        // Then
        assertThatExceptionOfType(AuthorizationMethodNotAcceptedException.class).isThrownBy(
                () -> authorizationService.authorizeAccountToCustomer(authorizedRequest))
                .withMessage("Please Use only READ or WRITE methods!");
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("It should not assign account to a given customer if the account does not belong to the grantor")
    void ItShouldNotAssignAccountToGivenCustomerIfCustomerDoesNotOwnsAccount() {
        // Given
        given(customerRepository.findById(MockData.CUSTOMER_2_ID)).willReturn(Optional.of(customer2));
        given(customerRepository.findById(MockData.CUSTOMER_3_ID)).willReturn(Optional.of(customer3));
        authorizedRequest = AuthorizedRequest.builder()
                .grantorId(MockData.CUSTOMER_2_ID)
                .granteeId(MockData.CUSTOMER_3_ID)
                .accountNumber(MockData.CUSTOMER_1_PAYMENT_ACCOUNT)
                .authorizationType(AccountAuthorizationType.WRITE)
                .build();
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
        // When
        // Then
        assertThatExceptionOfType(IbanDoesNotBelongsToGrantorException.class).isThrownBy(
                () -> authorizationService.authorizeAccountToCustomer(authorizedRequest))
                .withMessage("You dont have rights to assign this IBAN. Please use IBAN that belongs to you!");
        assertThat(customer2.getCustomerDetails().size()).isEqualTo(2);
    }
}