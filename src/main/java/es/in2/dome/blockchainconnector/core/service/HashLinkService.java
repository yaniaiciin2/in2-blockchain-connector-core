package es.in2.dome.blockchainconnector.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchainconnector.core.exception.InvalidHashlinkComparisonException;

public interface HashLinkService {
    void resolveHashlink(String dataLocation);
    String createHashLink(String id, String entityData);

}
