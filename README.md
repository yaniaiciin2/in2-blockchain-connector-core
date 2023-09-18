# Introduction
Blockchain Connector is one of the components used by the Access Node to interact between off-chain storage and on-chain
storage. It is a RESTFul API that provides a set of endpoints to interact with the Context Broker and Blockchain.

It is built using Java 17, Spring Boot 3.x, and Gradle.

## Main features:
- Subscribe to new entities stored in the Context Broker.
- Create Blockchain Events with the detailed data of the Context Broker Entity as attributes values.
- Publish Blockchain Events to a configured Blockchain Node.
- Query Blockchain Events to the Blockchain Node using Event Metadata as filters.
- Retrieve Entity Data using the ‘data location’ attribute of the Blockchain Event.
- Publish retrieved Entities to the configured Context Broker.

# Components

## OnChain Service

[![](https://mermaid.ink/img/pako:eNqFUj1rwzAQ_StCUwsJhI4aAm3SKU1ccEYtinSpj8qSK58KIeS_V7Y8hNi4msS9j3sn3ZVrb4AL3sJPBKdhi-orqFo6lk6jAqHGRjlie6UrdDAGynhqdcCG0LsxWoRU_jAHT3hGrTrSxjsK3loIY3pCK4Xu3RHSpYTwi3qi5Zv1-lt3RHZI8eXQ9z4JW67X880Fu69nh1lBbzkVUDAdQBG8OvMZTxbbKsNHX2T203O2nxLPuG561zahLA-bcbY9Fv_4PTyQYEOuObMHDVt2RsOvC_ayWrFixxe8hlArNGllrp1OcqqgBslFuho4q2hJculuiaoi-fLiNBcUIix4bEyaZ9gwLs7KtqkKBsmHfV7Dfhtvfw_26eo?type=png)](https://mermaid.live/edit#pako:eNqFUj1rwzAQ_StCUwsJhI4aAm3SKU1ccEYtinSpj8qSK58KIeS_V7Y8hNi4msS9j3sn3ZVrb4AL3sJPBKdhi-orqFo6lk6jAqHGRjlie6UrdDAGynhqdcCG0LsxWoRU_jAHT3hGrTrSxjsK3loIY3pCK4Xu3RHSpYTwi3qi5Zv1-lt3RHZI8eXQ9z4JW67X880Fu69nh1lBbzkVUDAdQBG8OvMZTxbbKsNHX2T203O2nxLPuG561zahLA-bcbY9Fv_4PTyQYEOuObMHDVt2RsOvC_ayWrFixxe8hlArNGllrp1OcqqgBslFuho4q2hJculuiaoi-fLiNBcUIix4bEyaZ9gwLs7KtqkKBsmHfV7Dfhtvfw_26eo)

The Orion-LD Notification Service is a component of the Blockchain Connector system that handles notifications received from the Orion-LD system and processes them to create and publish on-chain entities. This README provides an overview of the service's components and functionality.
Components

### 1. OrionLdNotificationController
- This is a RESTful controller responsible for receiving notifications from the Orion-LD system.
- It listens to POST requests at the "/notifications/orion-ld" endpoint.
- When a POST request is received, it expects a JSON payload representing an `OrionLdNotificationDTO`.
- The controller logs the received notification and then calls the `createAndPublishEntityToOnChain` method of the `OnChainEntityService` for further processing.

### 2. OnChainEntityServiceImpl
- This service class implements the `OnChainEntityService` interface.
- Its primary role is to process notifications and create and publish on-chain entities based on the received notifications.
- Here are the main steps performed by this service:
  - It receives an `OrionLdNotificationDTO` as input, representing a notification from the Orion-LD system.
  - It processes the notification by creating an on-chain entity called `DomeEvent`.
  - The `DomeEvent` is constructed based on data from the received notification.
  - The service utilizes the `hashLinkService` to create a hash link for the data, ensuring data integrity and security.
  - It publishes the `DomeEvent` to a blockchain node interface using HTTP requests.
  - If the publication is successful (HTTP status code 200), it logs a success message. If not, it throws a `RequestErrorException`.



## Dependencies

- **hashLinkService**: This service is used to create hash links for data, ensuring data integrity and security.

- **blockchainNodeIConfig**: Configuration for the blockchain node interface, including HTTP client setup.

- **blockchainNodeProperties**: Configuration properties for the blockchain node, such as API endpoints and settings.

## Offchain Service



[![](https://mermaid.ink/img/pako:eNqNkz1vAjEMhv-KlRkk1DEDQ6FT-ajKmsXN-TirueTI-agQ4r834UOiwFVkiuLXr5_Ezl7ZUJDSqqVNR97SlHEdsTYe0mowCltu0AssIwc_nE3zZs3-XjBHW7Gn-8CrC_bbVsgeFqnYf4IcXwThki1KqjcJXmJwjuJ9VijLSc5588KyW1HcsqV-bnNGvqGB4Xj8FICGT7LEW4Lr-K1nf_6x0ENmDRN0Dv7c4KHwaHHTh4wlkSlxnbRQxlDDFAVhFq4h-y37qEKdHvLsy9Q-S6bho_ty3FYXIglXXXj-vbLneajyLdsm-AJ-WCp4GY1g-Z47arwaqJpijVykKd5nf6OkopqM0mlbUImdE6OMPyQpdhJWO2-VltjRQHVNgXIZeqVLdG06pYIlxPnpZxw_yOEXMOAeMg?type=png)](https://mermaid.live/edit#pako:eNqNkz1vAjEMhv-KlRkk1DEDQ6FT-ajKmsXN-TirueTI-agQ4r834UOiwFVkiuLXr5_Ezl7ZUJDSqqVNR97SlHEdsTYe0mowCltu0AssIwc_nE3zZs3-XjBHW7Gn-8CrC_bbVsgeFqnYf4IcXwThki1KqjcJXmJwjuJ9VijLSc5588KyW1HcsqV-bnNGvqGB4Xj8FICGT7LEW4Lr-K1nf_6x0ENmDRN0Dv7c4KHwaHHTh4wlkSlxnbRQxlDDFAVhFq4h-y37qEKdHvLsy9Q-S6bho_ty3FYXIglXXXj-vbLneajyLdsm-AJ-WCp4GY1g-Z47arwaqJpijVykKd5nf6OkopqM0mlbUImdE6OMPyQpdhJWO2-VltjRQHVNgXIZeqVLdG06pYIlxPnpZxw_yOEXMOAeMg)

### 1. BlockchainNodeNotificationController
- This RESTful controller receives notifications from the Blockchain Node.
- It listens to POST requests at the "/notifications/blockchain-node" endpoint.
- When a POST request is received, it expects a JSON payload representing a `BlockchainNodeNotificationDTO`.
- The controller logs the received notification and then calls the `retrieveAndPublishEntityToOffChain` method of the `OffChainEntityService` for further processing.

### 2. OffChainEntityServiceImpl
- This service class implements the `OffChainEntityService` interface.
- Its primary role is to process notifications received from the Blockchain Node and perform the necessary steps to publish entities to the Orion-LD system.
- Here are the main steps performed by this service:
  - It receives a `BlockchainNodeNotificationDTO` as input, representing a notification from the Blockchain Node.
  - It retrieves the data location from the notification, which points to the entity to be published.
  - The service verifies the entity's integrity and security by using the `hashLinkService` to resolve the hashlink associated with the data location.
  - Once the entity is verified, it is published to the Orion-LD system using an HTTP POST request.
  - If the publication is successful, the service logs a success message. If not, it throws a `RequestErrorException`.

## Dependencies

- **hashLinkService**: This service is used to create hash links for data, ensuring data integrity and security.

- **applicationUtils**: Utility methods for making HTTP requests and handling responses.

- **orionLdProperties**: Configuration properties for the Orion-LD system, such as API endpoints and settings.

- **blockchainNodeIConfig**: Configuration for the blockchain node interface, including HTTP client setup and configuration.

This OffChain Service plays a crucial role in ensuring that data is seamlessly transferred from the Blockchain Node to the Orion-LD system with data integrity and security.





# Getting Started

## Prerequisites
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle](https://gradle.org/install/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [MongoDB](https://www.mongodb.com/)
- [Docker Desktop](https://www.docker.com/)
- [Go](https://golang.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Git](https://git-scm.com/)
- [Azure DevOps](https://azure.microsoft.com/en-us/services/devops/)
- [Orion-LD](https://github.com/FIWARE/context.Orion-LD/blob/develop/doc/manuals-ld/installation-guide-docker.md)

## Application profiles
- <b>*Default*</b>: this profile is only used to execute test during the CI/CD pipeline.
- <b>DEV</b>: this profile is used to execute the application in a Docker container.
- <b>TEST</b>: this profile is used to execute the application in a pre-production environment.
- <b>PROD</b>: this profile is used to execute the application in a production environment.

## API references (DEV environment)
- Swagger: http://localhost:8280/swagger-ui.html
- OpenAPI: http://localhost:8280/api-docs

## Installing
- Clone Blockchain Connector project and Alastria Red T to your local machine.
  ```git clone https://dev.azure.com/in2Dome/DOME/_git/in2-dome-blockchain_connector```
  ```git clone https://dev.azure.com/in2Dome/DOME/_git/in2-test-alastria_red_t```
- Open/Run Docker Desktop
- Navigate to the root folder of the in2-test-alastria_red_t and execute the docker-compose. This will execute the Alastria Red T that consists in 6 local nodes (1 boot, 1 regular, and 4 validators).
  ```cd in2-test-alastria_red_t```
  ```docker-compose up -d```
- Navigate to the root folder of the in2-dome-blockchain_connector and build the docker image. This will execute the Blockchain Connector Solution.
  ```cd in2-dome-blockchain_connector```
  ```docker build -t blk-conn .```
- Navigate to the root folder of the in2-dome-blockchain_connector/docker and execute the docker-compose. This will execute the Blockchain Connector Solution, the Orion Context Broker, and the MongoDB linked to Context Broker
  ```cd in2-dome-blockchain_connector/docker```
  ```docker-compose up -d```


## Orion-LD Documentation and Tools
- [Orion-LD Context Broker GitHub Repository with Documentation](https://github.com/FIWARE/context.Orion-LD)
- [Orion-LD Test Suite GitHub Repository](https://github.com/FIWARE/NGSI-LD_TestSuite/tree/master)
- [Orion-LD Context Broker Swagger UI](https://forge.etsi.org/swagger/ui/?url=https://forge.etsi.org/rep/NGSI-LD/NGSI-LD/-/raw/master/spec/updated/generated/full_api.json#/)

# License
- [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)


# Acknowledgments
We extend heartfelt gratitude to [FIWARE Foundation](https://www.fiware.org/foundation/), DOME teams project, [Alastria](https://alastria.io/), and [DigitelTS](https://digitelts.es/) for their invaluable contributions to our project. Their support, expertise, and resources have been pivotal in shaping this solution, driving innovation, and overcoming challenges. Without their collaboration, this project would not have been possible. We are honored to have worked with such visionary and innovative partners, and we look forward to future collaborations and successes together. Thank you for being essential pillars in our journey.

# Authors
- [IN2](https://in2.es), [Oriol Canadés](mailto:oriol.canades@in2.es)

# Document Version
- v0.0.2