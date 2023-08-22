package es.in2.dome.blockchain.connector.integration.contextbroker.service;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface NotificationService {
     void processNotification(String data);
     String extractField(String body, String name) throws JsonProcessingException;
}
