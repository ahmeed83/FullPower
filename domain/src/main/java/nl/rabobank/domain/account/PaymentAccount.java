package nl.rabobank.domain.account;

import lombok.Value;

@Value
public class PaymentAccount implements Account {
    String accountNumber;
    String accountHolderName;
    Double balance;
}
