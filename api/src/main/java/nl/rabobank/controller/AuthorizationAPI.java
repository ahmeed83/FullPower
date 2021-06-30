package nl.rabobank.controller;

import lombok.AllArgsConstructor;
import nl.rabobank.domain.authorizations.AuthorizedRequest;
import nl.rabobank.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Authorization API
 */
@RestController
@RequestMapping("/rabobank/api/v1/authorize-account")
@AllArgsConstructor
public class AuthorizationAPI {

    /**
     * Customer service.
     */
    private final AuthorizationService authorizationService;


    /**
     * Authorize given account customer (grantee).
     *
     * @param authorizedRequest authorizedRequest
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> authorizeAccountToCustomer(@Valid @RequestBody final AuthorizedRequest authorizedRequest) {
        authorizationService.authorizeAccountToCustomer(authorizedRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
