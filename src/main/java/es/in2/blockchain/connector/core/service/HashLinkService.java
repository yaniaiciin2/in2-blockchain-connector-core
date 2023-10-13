package es.in2.blockchain.connector.core.service;

public interface HashLinkService {
    String createHashLink(String id, String entityData);
    String resolveHashlink(String dataLocation);
    boolean compareHashLinksFromEntities(String dataLocation, String originOffChaiEntity);
    String extractHashLink(String url);
}
