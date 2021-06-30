package nl.rabobank.service;

import lombok.AllArgsConstructor;
import nl.rabobank.domain.customer.CustomerAccountDetails;
import nl.rabobank.exception.business.CustomerNotFoundException;
import nl.rabobank.data.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    /**
     * CustomerRepository.
     */
    private final CustomerRepository customerRepository;

    /**
     * Get all details for given customer.
     *
     * @param customerId customerId
     * @return customer details
     */
    public List<CustomerAccountDetails> getCustomerAccountDetails(final Long customerId) {
        final var customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        return customer.getCustomerDetails();
    }
}
