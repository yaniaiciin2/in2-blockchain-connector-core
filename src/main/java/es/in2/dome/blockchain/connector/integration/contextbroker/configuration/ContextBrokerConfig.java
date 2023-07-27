package es.in2.dome.blockchain.connector.integration.contextbroker.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutionException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ContextBrokerConfig {

    private final SubscriptionService subscriptionService;


    @Bean
    @Profile("!default")
    public void setDefaultSubscriptions() throws JsonProcessingException, ExecutionException, InterruptedException {
        subscriptionService.createSubscription();

    }

}
