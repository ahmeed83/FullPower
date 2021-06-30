package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class GranteeHasIBANAlreadyException extends ApplicationException {

    /**
     * Constructor.
     */
    public GranteeHasIBANAlreadyException() {
        super("Grantee already has this IBAN with this Type. Please use different IBAN!", HttpStatus.CONFLICT);
    }
}