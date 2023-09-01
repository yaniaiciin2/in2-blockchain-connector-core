package es.in2.dome.blockchain.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchain.connector.exception.InvalidHashlinkComparisonException;

public interface HashLinkResolutionService {

    void resolveHashlink(String datalocation) throws JsonProcessingException, InvalidHashlinkComparisonException;

}
