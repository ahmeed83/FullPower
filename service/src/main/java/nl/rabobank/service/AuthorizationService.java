package nl.rabobank.service;

import lombok.AllArgsConstructor;
import nl.rabobank.data.repository.AccountRepository;
import nl.rabobank.data.repository.CustomerRepository;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;
import nl.rabobank.domain.authorizations.AuthorizedRequest;
import nl.rabobank.domain.customer.Customer;
import nl.rabobank.domain.customer.CustomerAccountDetails;
import nl.rabobank.exception.business.AccountNotFoundException;
import nl.rabobank.exception.business.AuthorizationMethodNotAcceptedException;
import nl.rabobank.exception.business.CustomerNotFoundException;
import nl.rabobank.exception.business.GranteeHasIBANAlreadyException;
import nl.rabobank.exception.business.GrantorIdNotEqualGranteeIdException;
import nl.rabobank.exception.business.IbanDoesNotBelongsToGrantorException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizationService {

    /**
     * AccountRepository.
     */
    AccountRepository accountRepository;

    /**
     * CustomerRepository.
     */
    CustomerRepository customerRepository;

    /**
     * Authorize given account to a customer (grantee).
     *
     * @param authorizedRequest authorizedRequest
     */
    public void authorizeAccountToCustomer(final AuthorizedRequest authorizedRequest) {

        if (authorizedRequest.getGrantorId().equals(authorizedRequest.getGranteeId())) {
            throw new GrantorIdNotEqualGranteeIdException();
        }

        if (!isAuthorizationMethodAcceptable(authorizedRequest.getAuthorizationType())) {
            throw new AuthorizationMethodNotAcceptedException();
        }
        
        var customerGrantor = customerRepository.findById(authorizedRequest.getGrantorId())
                .orElseThrow(CustomerNotFoundException::new);
        var customerGrantee = customerRepository.findById(authorizedRequest.getGranteeId())
                .orElseThrow(CustomerNotFoundException::new);

        if (!isIbanBelongsToGrantor(customerGrantor, authorizedRequest.getAccountNumber())) {
            throw new IbanDoesNotBelongsToGrantorException();
        }

        if (hasGranteeThisIbanAlreadyWithTheSameAuthorizationType(customerGrantee, authorizedRequest.getAccountNumber(),
                                                                  authorizedRequest.getAuthorizationType())) {
            throw new GranteeHasIBANAlreadyException();
        }

        final var accountNumber = accountRepository.findAccountByAccountNumber(authorizedRequest.getAccountNumber())
                .orElseThrow(AccountNotFoundException::new);

        final var authorizedAccount = CustomerAccountDetails.builder()
                .account(accountNumber)
                .accountAuthorizationType(authorizedRequest.getAuthorizationType())
                .build();

        final var customer = customerGrantee.toBuilder().build();
        customer.getCustomerDetails().add(authorizedAccount);
        customerRepository.save(customer);
    }
    
    /**
     * Only READ and WRITE is accepted to be assigned to accounts.
     *
     * @param authorizationType authorizationType
     * @return check if the type READ or WRITE
     */
    private boolean isAuthorizationMethodAcceptable(final AccountAuthorizationType authorizationType) {
        return authorizationType.equals(AccountAuthorizationType.READ) || authorizationType.equals(
                AccountAuthorizationType.WRITE);
    }

    /**
     * Accounts can not be assigned twice to grantees, only if the authorization type is different.
     *
     * @param customerGrantee   customer grantee
     * @param accountNumber     account number
     * @param authorizationType authorization type
     * @return Check if the Grantee already has the given account assigned to him/her. And if the account is already
     * assigned, check if the authorization type is the same.
     */
    private boolean hasGranteeThisIbanAlreadyWithTheSameAuthorizationType(final Customer customerGrantee,
                                                                          final String accountNumber,
                                                                          final AccountAuthorizationType authorizationType) {
        return customerGrantee.getCustomerDetails()
                .stream()
                .filter(customerDetails -> customerDetails.getAccount().getAccountNumber().equals(accountNumber))
                .anyMatch(customerDetails -> customerDetails.getAccountAuthorizationType().equals(authorizationType));
    }

    /**
     * Grantor can not assigned accounts that are not belongs to him/her.
     *
     * @param customerGrantor customerGrantor
     * @param accountNumber   accountNumber
     * @return check if the given accounts belongs to the grantor.
     */
    private boolean isIbanBelongsToGrantor(final Customer customerGrantor, final String accountNumber) {
        return customerGrantor.getCustomerDetails()
                .stream()
                .filter(customer -> customer.getAccountAuthorizationType().equals(AccountAuthorizationType.OWNER))
                .anyMatch(authorizedAccount -> authorizedAccount.getAccount().getAccountNumber().equals(accountNumber));
    }
}
