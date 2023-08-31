package es.in2.dome.blockchain.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchain.connector.exception.HashLinkException;

public interface DomeEventService {
     String createDomeEvent (String type, String notificationdata, String id) throws JsonProcessingException, HashLinkException;
     String createHash(String resourceData, String id) throws HashLinkException;
}
