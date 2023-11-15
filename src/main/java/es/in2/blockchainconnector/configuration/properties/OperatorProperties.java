package es.in2.blockchainconnector.configuration.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;
import java.util.UUID;

/**
 * Configuration of the operator of the component.
 *
 * @param organizationId - operator OrganizationID information
 */
@Slf4j
@ConfigurationProperties(prefix = "operator")
public record OperatorProperties(String organizationId) {

    @ConstructorBinding
    public OperatorProperties(String organizationId) {
        this.organizationId = Optional.ofNullable(organizationId).orElse(UUID.randomUUID().toString());
    }

}