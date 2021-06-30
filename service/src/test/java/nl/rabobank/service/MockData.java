package nl.rabobank.service;

import nl.rabobank.domain.account.PaymentAccount;
import nl.rabobank.domain.account.SavingsAccount;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;
import nl.rabobank.domain.customer.Customer;
import nl.rabobank.domain.customer.CustomerAccountDetails;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    
    /*
    * Customer 1 owns two accounts CUSTOMER_GRANTOR_PAYMENT_ACCOUNT and CUSTOMER_GRANTOR_SAVING_ACCOUNT
    * Customer 2 own one account CUSTOMER_GRANTEE_PAYMENT_ACCOUNT and has permission to customer 1 account CUSTOMER_GRANTOR_PAYMENT_ACCOUNT
    */

    public static final long CUSTOMER_1_ID = 100L;
    public static final String CUSTOMER_1_PAYMENT_ACCOUNT = "NL11INGB0111123456";
    public static final String CUSTOMER_1_PAYMENT_NAME = "Ahmed Aziz";
    public static final double CUSTOMER_1_PAYMENT_BALANCE = 1000.00;
    public static final String CUSTOMER_1_SAVING_ACCOUNT = "NL22INGB0111123222";
    public static final String CUSTOMER_1_SAVING_NAME = "Ahmed Aziz";
    public static final double CUSTOMER_1_SAVING_BALANCE = 2000.00;

    public static final long CUSTOMER_2_ID = 200L;
    public static final String CUSTOMER_2_PAYMENT_NAME = "Mark van Rijswijk";
    public static final String CUSTOMER_2_PAYMENT_ACCOUNT = "NL22INGB0111123111";
    public static final double CUSTOMER_2_PAYMENT_BALANCE = 5500.00;
    
    public static final long CUSTOMER_3_ID = 300L;
    
    final static PaymentAccount customer_1_PaymentAccount = new PaymentAccount(CUSTOMER_1_PAYMENT_ACCOUNT,
                                                                               CUSTOMER_1_PAYMENT_NAME,
                                                                               CUSTOMER_1_PAYMENT_BALANCE);
    final static SavingsAccount customer_1_SavingAccount = new SavingsAccount(CUSTOMER_1_SAVING_ACCOUNT,
                                                                              CUSTOMER_1_SAVING_NAME,
                                                                              CUSTOMER_1_SAVING_BALANCE);

    final static PaymentAccount customer_2_PaymentAccount = new PaymentAccount(CUSTOMER_2_PAYMENT_ACCOUNT,
                                                                               CUSTOMER_2_PAYMENT_NAME,
                                                                               CUSTOMER_2_PAYMENT_BALANCE);
    
    // Customer has two accounts, both of them are his own accounts. 
    static Customer generateTestCustomer1() {
        CustomerAccountDetails customerDetailsGrantorPayment = getCustomerDetails(
                CustomerAccountDetails.builder().account(customer_1_PaymentAccount), AccountAuthorizationType.OWNER);
        CustomerAccountDetails customerDetailsGrantorSaving = getCustomerDetails(
                CustomerAccountDetails.builder().account(customer_1_SavingAccount), AccountAuthorizationType.OWNER);

        List<CustomerAccountDetails> authorizedAccountsAhmed = new ArrayList<>();
        authorizedAccountsAhmed.add(customerDetailsGrantorPayment);
        authorizedAccountsAhmed.add(customerDetailsGrantorSaving);

        return Customer.builder().id(CUSTOMER_1_ID).customerDetails(authorizedAccountsAhmed).build();
    }

    // Customer has two accounts, own account and another account where he has the READ permission. 
    static Customer generateTestCustomer2() {
        CustomerAccountDetails customerDetailsGranteePayment = getCustomerDetails(
                CustomerAccountDetails.builder().account(customer_2_PaymentAccount), AccountAuthorizationType.OWNER);
        CustomerAccountDetails customerDetailsGrantorPayment = getCustomerDetails(
                CustomerAccountDetails.builder().account(customer_1_PaymentAccount), AccountAuthorizationType.READ);
        
        List<CustomerAccountDetails> authorizedAccountsAhmed = new ArrayList<>();
        authorizedAccountsAhmed.add(customerDetailsGrantorPayment);
        authorizedAccountsAhmed.add(customerDetailsGranteePayment);

        return Customer.builder().id(CUSTOMER_2_ID).customerDetails(authorizedAccountsAhmed).build();
    }

    private static CustomerAccountDetails getCustomerDetails(final CustomerAccountDetails.CustomerAccountDetailsBuilder account,
                                                             final AccountAuthorizationType authorizationType) {
        return account.accountAuthorizationType(authorizationType).build();
    }
    
    public static Customer generateTestCustomer3() {
        List<CustomerAccountDetails> authorizedAccountsAhmed = new ArrayList<>();
        return Customer.builder().id(CUSTOMER_3_ID).customerDetails(authorizedAccountsAhmed).build();
    }
}
