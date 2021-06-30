package nl.rabobank.domain.account;

import lombok.Value;

@Value
public class SavingsAccount implements Account {
    String accountNumber;
    String accountHolderName;
    Double balance;
}
