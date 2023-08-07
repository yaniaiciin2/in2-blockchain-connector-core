package es.in2.dome.blockchain.connector.integration.contextbroker.domain.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEndpointDTO {
    private String uri;
    private String accept;
}