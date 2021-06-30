package nl.rabobank.domain.customer;

import lombok.Builder;
import lombok.Value;
import nl.rabobank.domain.account.Account;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;

@Value
@Builder(toBuilder = true)
public class CustomerAccountDetails {
    Account account;
    AccountAuthorizationType accountAuthorizationType;
}
