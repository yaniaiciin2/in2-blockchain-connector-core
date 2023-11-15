package es.in2.blockchainconnector.core.service.impl;

import es.in2.blockchainconnector.core.service.BrokerEntityValidationService;
import es.in2.blockchainconnector.integration.dltadapter.domain.DLTNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static es.in2.blockchainconnector.core.utils.ApplicationUtils.calculateSHA256Hash;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEntityValidationServiceImpl implements BrokerEntityValidationService {

    private static final String HASHLINK_PREFIX = "\\?hl=";

    @Override
    public Mono<String> validateEntityIntegrity(String brokerEntity, DLTNotificationDTO dltNotificationDTO) {
        try {
            // Create Hash from the retrieved entity
            String entityHash = calculateSHA256Hash(brokerEntity);
            log.debug(" > Entity hash: {}", entityHash);
            // Get Hash from the dataLocation
            // Get URL from the DLTNotificationDTO.dataLocation()
            String dataLocation = dltNotificationDTO.dataLocation();
            log.debug(" > Data location: {}", dataLocation);
            String sourceBrokerEntityURL = Arrays.stream(dataLocation.split(HASHLINK_PREFIX))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            log.debug(" > Source broker entity URL: {}", sourceBrokerEntityURL);
            // Get Hash from the dataLocation
            String sourceEntityHash = dataLocation
                    .replace(sourceBrokerEntityURL, "")
                    .replace("?hl=", "");
            log.debug(" > Source entity hash: {}", sourceEntityHash);
            // Compare both hashes
            if (entityHash.equals(sourceEntityHash)) {
                log.debug(" > Entity integrity is valid");
                return Mono.just(brokerEntity);
            } else {
                log.error(" > Entity integrity is not valid");
                return Mono.error(new IllegalArgumentException("Entity integrity cannot be validated"));
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Error validating entity integrity: {}", e.getMessage(), e.getCause());
            return Mono.error(e);
        }
    }

}
