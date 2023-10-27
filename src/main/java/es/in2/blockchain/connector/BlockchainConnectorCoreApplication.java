package es.in2.blockchain.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BlockchainConnectorCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainConnectorCoreApplication.class, args);
	}

}
