package es.in2.dome.blockchain.connector.integration.contextbroker.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SubscriptionService {
    void createDefaultSubscription() throws JsonProcessingException;
}
