package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerConfigApi;
import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerConfigSubscription;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.dto.EntityDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.dto.SubscriptionDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.ContextBrokerSubscription;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.SubscriptionEntity;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.SubscriptionNotification;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.SubscriptionNotificationEndpoint;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.SubscriptionService;
import es.in2.dome.blockchain.connector.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ContextBrokerConfigApi contextBrokerProperties;
    private final ContextBrokerConfigSubscription contextBrokerSubscriptionProperties;
    private final ApplicationUtils applicationUtils;

    @Profile("!default")
    public void createDefaultSubscription() throws JsonProcessingException, InterruptedException, ExecutionException {

        log.debug("Creating default subscription...");

        log.debug("Getting subscriptions from Context Broker...");
        List<SubscriptionDTO> subscriptionList = getContextBrokerSubscriptions();

        log.debug("Checking if a matching subscription already exists...");
        boolean subscriptionComparisonResult = subscriptionList.stream()
                .anyMatch(subscription -> subscription.getType().equals(contextBrokerSubscriptionProperties.getType()) &&
                        subscription.getEntities().size() == contextBrokerSubscriptionProperties.getEntities().size() &&
                        compareEntityLists(subscription.getEntities(), contextBrokerSubscriptionProperties.getEntities()));

        if(subscriptionList.isEmpty()) {
            createSubscription();
        } else if (!subscriptionComparisonResult) {
            // update subscription adding new entities_type
        } else {
            log.debug("Subscription does not have new data. No action required.");
        }

    }

    private void createSubscription() throws JsonProcessingException {

        log.debug("Building subscription...");
        ContextBrokerSubscription newSubscription = buildSubscription();

        // Parse subscription to JSON String object.
        String requestBody = new ObjectMapper().writeValueAsString(newSubscription);
        log.debug("Posting subscription to Context Broker: {}", requestBody);

        // Post subscription to Context Broker
        applicationUtils.postRequest(contextBrokerProperties.getSubscriptionUrl(), requestBody);
        log.debug("Subscription created successfully");

    }

    private boolean compareEntityLists(List<EntityDTO> list1, List<Map<String, Object>> list2) {
        return list1.size() == list2.size() &&
                IntStream.range(0, list1.size())
                        .allMatch(i -> compareEntities(list1.get(i), list2.get(i)));
    }

    private boolean compareEntities(EntityDTO entity1, Map<String, Object> entity2Map) {
        String type1 = entity1.getType();
        String type2 = (String) entity2Map.get("type");
        return type1.equals(type2);
    }

    private ContextBrokerSubscription buildSubscription() {
        // Create entities list
        List<SubscriptionEntity> entities = new ArrayList<>();
        contextBrokerSubscriptionProperties.getEntities().forEach(item -> entities.add(SubscriptionEntity.builder()
                .type(item.get("type").toString())
                .build()));
        // Create subscription
        return ContextBrokerSubscription.builder()
                .entities(entities)
                .id(contextBrokerSubscriptionProperties.getIdPrefix() + contextBrokerSubscriptionProperties.getName() + new Date().getTime())
                .type(contextBrokerSubscriptionProperties.getType())
                .notification(new SubscriptionNotification(new SubscriptionNotificationEndpoint
                        (URLDecoder.decode(contextBrokerSubscriptionProperties.getNotificationEndpointUri(), StandardCharsets.UTF_8))))
                .build();
    }

    private List<SubscriptionDTO> getContextBrokerSubscriptions() throws JsonProcessingException {
        String response = applicationUtils.getRequest(contextBrokerProperties.getSubscriptionUrl());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SubscriptionDTO.class));
    }

}
