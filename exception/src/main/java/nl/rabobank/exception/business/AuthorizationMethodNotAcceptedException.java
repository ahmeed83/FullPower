package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class AuthorizationMethodNotAcceptedException extends ApplicationException {

    /**
     * Constructor.
     */
    public AuthorizationMethodNotAcceptedException() {
        super("Please Use only READ or WRITE methods!", HttpStatus.FORBIDDEN);
    }
}