package nl.rabobank.data.repository;

import nl.rabobank.domain.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    
    Optional<Account> findAccountByAccountNumber(final String accountNumber);
}