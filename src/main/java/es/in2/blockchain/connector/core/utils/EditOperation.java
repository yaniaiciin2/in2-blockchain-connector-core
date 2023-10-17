package es.in2.blockchain.connector.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EditOperation {
    STATUS("STATUS"),
    HASH("HASH");

    private final String description;
}
