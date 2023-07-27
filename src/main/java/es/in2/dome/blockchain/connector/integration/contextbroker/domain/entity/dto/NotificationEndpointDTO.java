package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEndpointDTO {
    private String uri;
    private String accept;
}