package nl.rabobank;

import lombok.AllArgsConstructor;
import nl.rabobank.domain.authorizations.AccountAuthorizationType;
import nl.rabobank.domain.customer.CustomerAccountDetails;
import nl.rabobank.domain.account.PaymentAccount;
import nl.rabobank.domain.account.SavingsAccount;
import nl.rabobank.data.MongoConfiguration;
import nl.rabobank.data.repository.AccountRepository;
import nl.rabobank.data.repository.CustomerRepository;
import nl.rabobank.domain.customer.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Import(MongoConfiguration.class)
@AllArgsConstructor
public class RaboAssignmentApplication implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    public static void main(final String[] args) {
        SpringApplication.run(RaboAssignmentApplication.class, args);
    }

    //TEST DATA used for demo purposes. 
    @Override
    public void run(String... args) {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        final var ahmedPayment = new PaymentAccount("NL11INGB0111123456", "Ahmed Aziz", 1000.00);
        final var ahmedSaving = new SavingsAccount("NL22INGB0111123222", "Ahmed Aziz", 2000.00);
        final var eefPayment = new PaymentAccount("NL22INGB0111123111", "Eef van Rijswijk", 5500.00);
        final var agePayment = new SavingsAccount("NL22INGB0111123333", "Age van Schaick", 9500.00);
        final var rutgerSaving = new SavingsAccount("NL22INGB0111123444", "Rutger Rouschop", 3200.00);
        final var abdPayment = new PaymentAccount("NL22INGB0111123255", "Abdirahman Baijens", 1500.00);
        final var neclaSaving = new SavingsAccount("NL22INGB0111123666", "Necla van der Brugge", 3300.00);
        final var matPayment = new PaymentAccount("NL22INGB0111123777", "Matthis de Rond", 8700.00);
        accountRepository.save(ahmedPayment);
        accountRepository.save(ahmedSaving);
        accountRepository.save(eefPayment);
        accountRepository.save(agePayment);
        accountRepository.save(rutgerSaving);
        accountRepository.save(abdPayment);
        accountRepository.save(neclaSaving);
        accountRepository.save(matPayment);

        var authorizedAccountAhmedPayment = CustomerAccountDetails.builder().account(ahmedPayment).accountAuthorizationType(
                AccountAuthorizationType.OWNER).build();
        var authorizedAccountAhmedSaving = CustomerAccountDetails.builder().account(ahmedSaving).accountAuthorizationType(
                AccountAuthorizationType.OWNER).build();
        var authorizedAccountAhmedEefR = CustomerAccountDetails.builder().account(eefPayment).accountAuthorizationType(
                AccountAuthorizationType.READ).build();
        var authorizedAccountAhmedEefW = CustomerAccountDetails.builder().account(eefPayment).accountAuthorizationType(
                AccountAuthorizationType.WRITE).build();
        var authorizedAccountAhmedAge = CustomerAccountDetails.builder().account(agePayment).accountAuthorizationType(
                AccountAuthorizationType.WRITE).build();
        List<CustomerAccountDetails> authorizedAccountsAhmed = new ArrayList<>();
        authorizedAccountsAhmed.add(authorizedAccountAhmedPayment);
        authorizedAccountsAhmed.add(authorizedAccountAhmedSaving);
        authorizedAccountsAhmed.add(authorizedAccountAhmedEefR);
        authorizedAccountsAhmed.add(authorizedAccountAhmedEefW);
        authorizedAccountsAhmed.add(authorizedAccountAhmedAge);

        final var userIdAhmed = 100L;
        customerRepository.save(Customer.builder().id(userIdAhmed).customerDetails(authorizedAccountsAhmed).build());


        var authorizedAccountMat = CustomerAccountDetails.builder().account(matPayment).accountAuthorizationType(
                AccountAuthorizationType.OWNER).build();
        var authorizedAccountMatAhmed = CustomerAccountDetails.builder().account(ahmedSaving).accountAuthorizationType(
                AccountAuthorizationType.READ).build();
        var authorizedAccountMatEEF = CustomerAccountDetails.builder().account(eefPayment).accountAuthorizationType(
                AccountAuthorizationType.WRITE).build();
        var authorizedAccountMatAbd = CustomerAccountDetails.builder().account(eefPayment).accountAuthorizationType(
                AccountAuthorizationType.READ).build();
        List<CustomerAccountDetails> authorizedAccountsMat = new ArrayList<>();
        authorizedAccountsMat.add(authorizedAccountMat);
        authorizedAccountsMat.add(authorizedAccountMatAhmed);
        authorizedAccountsMat.add(authorizedAccountMatEEF);
        authorizedAccountsMat.add(authorizedAccountMatAbd);

        final var userIdMat = 200L;
        customerRepository.save(Customer.builder().id(userIdMat).customerDetails(authorizedAccountsMat).build());
    }
}
