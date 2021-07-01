package nl.rabobank.exception.business;

import nl.rabobank.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public class GrantorIdNotEqualGranteeIdException extends ApplicationException {

    /**
     * Constructor.
     */
    public GrantorIdNotEqualGranteeIdException() {
        super("Please use another customer id. This account is already assigned to you!", HttpStatus.CONFLICT);
    }
}