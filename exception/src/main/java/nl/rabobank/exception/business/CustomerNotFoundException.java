package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class CustomerNotFoundException extends ApplicationException {

    /**
     * Constructor.
     */
    public CustomerNotFoundException() {
        super("Customer not found. Please use different customer ID", HttpStatus.NOT_FOUND);
    }
}