package es.in2.blockchain.connector.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditStatus {
    RECEIVED("RECIEVED"),
    POST_TO_BLOCKCHAIN("POST TO BLOCKCHAIN"),
    ERROR("ERROR"),
    CREATED("CREATED");

    private final String description;
}
