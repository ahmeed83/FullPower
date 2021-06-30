package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class AccountNotFoundException extends ApplicationException {

    /**
     * Constructor.
     */
    public AccountNotFoundException() {
        super("Given Account not found. Please use different account", HttpStatus.NOT_FOUND);
    }
}