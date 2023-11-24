package es.in2.blockchainconnector.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTrader {

    PRODUCER("producer"),
    CONSUMER("consumer");

    private final String description;

}
