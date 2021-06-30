package nl.rabobank.controller;

import lombok.AllArgsConstructor;
import nl.rabobank.domain.customer.CustomerAccountDetails;
import nl.rabobank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer API.
 */
@RestController
@RequestMapping("rabobank/api/v1/customer-account-details")
@AllArgsConstructor
public class CustomerAPI {

    /**
     * Customer service.
     */
    private final CustomerService customerService;

    /**
     * Rest endpoint to get customer accounts belongs to the user.
     *
     * @return list of customer account details. 
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<CustomerAccountDetails>> getCustomerAccountDetails(@PathVariable final Long customerId) {
        return new ResponseEntity<>(customerService.getCustomerAccountDetails(customerId), HttpStatus.OK);
    }
}
