package es.in2.dome.blockchain.connector.integration.contextbroker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;

public interface DomeEventService {
     String createDomeEvent (String type, String notificationdata) throws JsonProcessingException, HashLinkException;
}
