package es.in2.blockchain.connector.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditStatus {
    RECEIVED("RECEIVED"),
    PUBLISHED("PUBLISHED"),
    CREATED("CREATED");

    private final String description;
}
