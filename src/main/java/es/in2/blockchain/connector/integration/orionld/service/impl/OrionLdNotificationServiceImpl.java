package es.in2.blockchain.connector.integration.orionld.service.impl;

import es.in2.blockchain.connector.core.service.OnChainService;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.service.OrionLdNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrionLdNotificationServiceImpl implements OrionLdNotificationService {

    private final OnChainService onChainService;

    @Override
    public void processNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        // retrieve data from notification DTO
        onChainService.createAndPublishEntityToOnChain(orionLdNotificationDTO);
    }
}
