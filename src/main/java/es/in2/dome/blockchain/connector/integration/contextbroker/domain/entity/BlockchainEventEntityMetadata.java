package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity;


import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockchainEventEntityMetadata {

   private ArrayList<String> metadata = new ArrayList<>();
}
