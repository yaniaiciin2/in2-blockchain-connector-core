package es.in2.blockchainconnector.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.configuration.properties.BrokerAdapterProperties;
import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.exception.JsonReadingException;
import es.in2.blockchainconnector.exception.RequestErrorException;
import es.in2.blockchainconnector.service.BrokerEntityPublicationService;
import es.in2.blockchainconnector.service.BrokerEntityRetrievalService;
import es.in2.blockchainconnector.service.BrokerEntityValidationService;
import es.in2.blockchainconnector.service.SourceBrokerDataRetrievalServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static es.in2.blockchainconnector.utils.Utils.getRequest;
import static es.in2.blockchainconnector.utils.Utils.getRequestResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceBrokerDataRetrievalServiceImplFacade implements SourceBrokerDataRetrievalServiceFacade {

    private final BrokerEntityRetrievalService brokerEntityRetrievalService;
    private final BrokerEntityValidationService brokerEntityValidationService;
    private final BrokerEntityPublicationService brokerEntityPublicationService;
    private final BrokerAdapterProperties brokerAdapterProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> retrieveAndPublishABrokerEntityIntContextBroker(DLTNotificationDTO dltNotificationDTO) {
        log.info("Retrieving entity from source broker: {}", dltNotificationDTO.toString());
        return brokerEntityRetrievalService.retrieveEntityFromSourceBroker(dltNotificationDTO)
                .flatMap(entityString -> {
                    log.info("Entity retrieved successfully: {}", entityString);
                    return brokerEntityValidationService.validateEntityIntegrity(entityString, dltNotificationDTO);
                })
                .flatMap(validatedEntity -> {
                    log.info("Entity validated successfully: {}", validatedEntity);
                    return handleBrokerResponse(validatedEntity);
                    // todo delete
                })
                .doOnTerminate(() -> log.info("Entity retrieval, validation, and publication completed"));
    }

    private Mono<Void> handleBrokerResponse(String validatedEntity) {
        try {
            // Depending on the status code it will decide if update or publish
            CompletableFuture<HttpResponse<String>> retrievedEntity = getRequest(brokerAdapterProperties.domain() +
                    brokerAdapterProperties.paths().entities() +
                    "/" + extractIdFromEntity(validatedEntity));

            int responseCode = retrievedEntity.thenApply(HttpResponse::statusCode).join();

            if (responseCode == 200) {
                log.info(" > Entity exists");
                return brokerEntityPublicationService.updateEntityToBroker(validatedEntity);
            } else if (responseCode == 404) {
                log.info(" > Entity doesn't exist");
                return brokerEntityPublicationService.publishEntityToBroker(validatedEntity);
            } else {
                log.warn("Unhandled response status code: {}", responseCode);
                return Mono.error(new RequestErrorException("Unhandled response status code: " + responseCode));
            }


        } catch (Exception e) {
            log.error("Error handling response", e);
            return Mono.error(e);
        }
    }

    private String extractIdFromEntity(String entity) {
        try {
            JsonNode jsonNode = objectMapper.readTree(entity);
            return jsonNode.get("id").asText();
        } catch (Exception e) {
            throw new JsonReadingException("Error while extracting data from entity");
        }
    }

}
