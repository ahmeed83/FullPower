package nl.rabobank.domain.authorizations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedRequest {
    @NonNull Long granteeId;
    @NonNull Long grantorId;
    @NonNull String accountNumber;
    @NonNull AccountAuthorizationType authorizationType;
}
