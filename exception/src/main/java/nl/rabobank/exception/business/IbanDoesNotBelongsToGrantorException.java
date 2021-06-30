package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class IbanDoesNotBelongsToGrantorException extends ApplicationException {

    /**
     * Constructor.
     */
    public IbanDoesNotBelongsToGrantorException() {
        super("You dont have rights to assign this IBAN. Please use IBAN that belongs to you!", HttpStatus.FORBIDDEN);
    }
}