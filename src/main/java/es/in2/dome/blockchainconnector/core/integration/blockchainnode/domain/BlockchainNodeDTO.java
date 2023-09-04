package es.in2.dome.blockchainconnector.core.integration.blockchainnode.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainNodeDTO {

    private String rpcAddress;
    private String publicKeyHex;

}
