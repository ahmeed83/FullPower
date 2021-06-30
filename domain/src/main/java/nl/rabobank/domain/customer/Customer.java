package nl.rabobank.domain.customer;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Customer {
    Long id;
    List<CustomerAccountDetails> customerDetails;
}
 