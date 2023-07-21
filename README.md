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
```docker build -t blockchain-connector .```
- Navigate to the root folder of the in2-dome-blockchain_connector/docker and execute the docker-compose. This will execute the Blockchain Connector Solution, the Orion Context Broker, and the MongoDB linked to Context Broker
```cd in2-dome-blockchain_connector/docker```
```docker-compose up -d```

## Running the tests

## Deployment

# Contribute

## Orion-LD Documentation and Tools
- [Orion-LD Context Broker GitHub Repository with Documentation](https://github.com/FIWARE/context.Orion-LD)
- [Orion-LD Test Suite GitHub Repository](https://github.com/FIWARE/NGSI-LD_TestSuite/tree/master)
- [Orion-LD Context Broker Swagger UI](https://forge.etsi.org/swagger/ui/?url=https://forge.etsi.org/rep/NGSI-LD/NGSI-LD/-/raw/master/spec/updated/generated/full_api.json#/)

# License
- [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## Third Party Licenses

# Acknowledgments
We extend heartfelt gratitude to [FIWARE Foundation](https://www.fiware.org/foundation/), DOME teams project, [Alastria](https://alastria.io/), and [DigitelTS](https://digitelts.es/) for their invaluable contributions to our project. Their support, expertise, and resources have been pivotal in shaping this solution, driving innovation, and overcoming challenges. Without their collaboration, this project would not have been possible. We are honored to have worked with such visionary and innovative partners, and we look forward to future collaborations and successes together. Thank you for being essential pillars in our journey.

# Authors
- [IN2](https://in2.es), [Oriol Canadés](mailto:oriol.canades@in2.es)

# Document Version
- v0.0.2