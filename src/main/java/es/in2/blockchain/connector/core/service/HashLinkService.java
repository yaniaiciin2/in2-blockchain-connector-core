package es.in2.blockchain.connector.core.service;

import reactor.core.publisher.Mono;

public interface HashLinkService {
    Mono<String> createHashLink(String id, String data);

    Mono<String> resolveHashlink(String dataLocation);

    Mono<Boolean> compareHashLinksFromEntities(String retrievedEntity, String originOffChainEntity);

    Mono<String> extractHashLink(String url);
}
