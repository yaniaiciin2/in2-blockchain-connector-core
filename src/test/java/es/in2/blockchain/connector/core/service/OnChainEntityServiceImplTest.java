package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.core.service.impl.OnChainEntityServiceImpl;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeIConfig;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeProperties;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

 class OnChainEntityServiceImplTest {

    @Mock
    private HashLinkService hashLinkService;

    @Mock
    private BlockchainNodeIConfig blockchainNodeIConfig;

    @Mock
    private BlockchainNodeProperties blockchainNodeProperties;

    @InjectMocks
    private OnChainEntityServiceImpl onChainEntityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        onChainEntityService = new OnChainEntityServiceImpl(hashLinkService, blockchainNodeIConfig, blockchainNodeProperties);
    }

    @Test
    void testCreateAndPublishEntityToOnChainWithHashLinkServiceError()  {
        // Arrange
        OrionLdNotificationDTO notificationDTO = createNotificationDTO();

        when(hashLinkService.createHashLink(any(), any())).thenThrow(new RequestErrorException("HashLink creation error"));

        // Act and Assert
        assertThrows(RequestErrorException.class, () -> onChainEntityService.createAndPublishEntityToOnChain(notificationDTO));
    }




     // Create sample OrionLdNotificationDTO for testing
    private OrionLdNotificationDTO createNotificationDTO() {
        OrionLdNotificationDTO notificationDTO = new OrionLdNotificationDTO();
        Map<String, Object> data = new HashMap<>();
        data.put("type", "SampleType");
        data.put("id", "SampleId");
        notificationDTO.setData(List.of(data));
        return notificationDTO;
    }
}
