package es.in2.dome.blockchain.connector.integration.contextbroker.domain;



import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerApiConfig;
import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerSubscriptionConfig;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.dto.EntityDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.dto.SubscriptionDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.*;


class SubscriptionServiceTests {

    @Mock
    private ContextBrokerApiConfig contextBrokerProperties;

    @Mock
    private ContextBrokerSubscriptionConfig contextBrokerSubscriptionProperties;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCompareEntityLists() throws Exception {
        // Arrange
        List<EntityDTO> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();

        Map<String, Object> entity1Map1 = new HashMap<>();
        entity1Map1.put("type", "type1");
        Map<String, Object> entity1Map2 = new HashMap<>();
        entity1Map2.put("type", "type2");

        list1.add(new EntityDTO("type1"));
        list1.add(new EntityDTO("type2"));

        list2.add(entity1Map1);
        list2.add(entity1Map2);

        SubscriptionServiceImpl subscriptionService = new SubscriptionServiceImpl(contextBrokerProperties, contextBrokerSubscriptionProperties);

        // Act
        Method compareEntityListsMethod = SubscriptionServiceImpl.class.getDeclaredMethod("compareEntityLists", List.class, List.class);
        compareEntityListsMethod.setAccessible(true);
        boolean result = (boolean) compareEntityListsMethod.invoke(subscriptionService, list1, list2);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testCompareEntities() throws Exception {
        // Arrange
        EntityDTO entity1 = new EntityDTO("type1");
        Map<String, Object> entity2Map = new HashMap<>();
        entity2Map.put("type", "type1");

        SubscriptionServiceImpl subscriptionService = new SubscriptionServiceImpl(contextBrokerProperties, contextBrokerSubscriptionProperties);

        // Act
        Method compareEntitiesMethod = SubscriptionServiceImpl.class.getDeclaredMethod("compareEntities", EntityDTO.class, Map.class);
        compareEntitiesMethod.setAccessible(true);
        boolean result = (boolean) compareEntitiesMethod.invoke(subscriptionService, entity1, entity2Map);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testParseSubscriptionList() throws Exception {
        // Arrange
        String responseBody = """
                [{"id":"urn:ngsi-ld:Subscription:default1690282910700","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"}],"status":"active","isActive":true,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}},
                {"id":"urn:ngsi-ld:Subscription:default1690283975761","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"}],"status":"paused","isActive":false,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}},
                {"id":"urn:ngsi-ld:Subscription:default1690285284362","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"}],"status":"paused","isActive":false,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}},
                {"id":"urn:ngsi-ld:Subscription:default1690286655249","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"}],"status":"paused","isActive":false,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}},
                {"id":"urn:ngsi-ld:Subscription:default1690286750383","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"}],"status":"paused","isActive":false,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}},
                {"id":"urn:ngsi-ld:Subscription:default1690447667491","type":"Subscription","entities":[{"type":"https://smart-data-models.github.io/data-models/terms.jsonld#/definitions/product"},{"type":"productSpecification"},{"type":"productOffering"}],"status":"paused","isActive":false,"notification":{"format":"normalized","endpoint":{"uri":"http://blockchain-connector:8280/notifications","accept":"application/json"},"status":"ok"}}]""";


        SubscriptionServiceImpl subscriptionService = new SubscriptionServiceImpl(contextBrokerProperties, contextBrokerSubscriptionProperties);

        Method parseSubscriptionList = SubscriptionServiceImpl.class.getDeclaredMethod("parseSubscriptionList", String.class);
        parseSubscriptionList.setAccessible(true);

        List<SubscriptionDTO> subscriptionDTOList = (List<SubscriptionDTO>) parseSubscriptionList.invoke(subscriptionService, responseBody);



        // Assert
        Assertions.assertEquals(6, subscriptionDTOList.size());
    }
}
