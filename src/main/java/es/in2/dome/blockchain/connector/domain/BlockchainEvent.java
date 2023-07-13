package es.in2.dome.blockchain.connector.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockchainEvent {

    /**
        The Event-type attribute indicates the type or category of the event. It represents the nature or purpose of
        the event within the blockchain network, allowing participants to distinguish between different event types
        and handle them accordingly.
     */
    private String eventType;

    /**
        The Timestamp captures the exact time when the event is built. It serves as a chronological reference, enabling
        participants to establish the temporal order of events within the blockchain network. The timestamp ensures
        data consistency and provides a reliable record of when the event occurred.

        Please note that, at the time of writing the DOME AR document, the JAdES signature format does not support
        Qualified Timestamp (Baseline-T). As a result, the decision has been made to utilize a Non-Qualified Timestamp
        (Baseline-B) as an alternative solution. This temporary measure allows for the continued implementation of
        timestamp functionality until a suitable solution for incorporating Qualified Timestamps can be found.
     */
    private String timestamp;

    /**
        The Data location specifies the storage or location of the data associated with the event. It denotes the
        address or identifier of the data within the blockchain network, facilitating efficient retrieval and
        verification of the event data. To expand on this point in the section, Safe pointer to the origin data in the
        blockchain transaction: the Hashlink of the document.
     */
    private String dataLocation;

    /**
        The Filter relevant meta-data contains additional information or metadata related to the event. This metadata
        provides context and relevant details about the event, which can be used for filtering or processing purposes.
        Participants can utilize this attribute to extract specific information relevant to their needs, or apply
        filters to manage and handle events effectively.
     */
    private String metadata;

}
