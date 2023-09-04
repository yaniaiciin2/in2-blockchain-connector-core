package es.in2.dome.blockchainconnector.core.service;

import es.in2.dome.blockchainconnector.core.domain.dto.DomeEventDTO;
import es.in2.dome.blockchainconnector.core.integration.orionld.domain.OrionLdNotificationDTO;

public interface DomeEventService {
     DomeEventDTO createDomeEvent(OrionLdNotificationDTO orionLdNotificationDTO);
}
