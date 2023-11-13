package es.in2.blockchainconnector.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration to establish eIDAS organization ID.
 * @param id - Desired organization ID.
 */

@Component
public record OperatorProperties(@Value("${operator.organization.id}") String id) {
}
