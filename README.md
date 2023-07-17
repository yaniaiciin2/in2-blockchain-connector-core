# Introduction 
Blockchain Connector is one of the components used by the Access Node to interact between off-chain storage and on-chain 
storage. It is a RESTFul API that provides a set of endpoints to interact with the Context Broker and Blockchain. 

It is built using Java 17, Spring Boot, and Gradle.

# Getting Started

## Prerequisites
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle](https://gradle.org/install/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [MongoDB](https://www.mongodb.com/)
- [Docker Desktop](https://www.docker.com/)
## Software profiles 
- <b>*Default*</b>: this profile is only used to execute test during the CI/CD pipeline.
- <b>DEV</b>: this profile is used to execute the application in a Docker container.
- <b>TEST</b>: this profile is used to execute the application in a pre-production environment.
- <b>PROD</b>: this profile is used to execute the application in a production environment.

## API references
- Swagger: http://localhost:8280/swagger-ui.html
- OpenAPI: http://localhost:8280/api-docs

## Installing
- Clone the repository to your local machine: $ git clone https://dev.azure.com/in2Dome/DOME/_git/in2-dome-blockchain_connector
- Build the image: $ docker build -t blockchain-connector .

## Running the tests

## Deployment

# Contribute

# License

# Acknowledgments

# References

# Authors
- [IN2](https://in2.es), [Oriol Canadés](mailto:oriol.canades@in2.es)

# Versioning
- v0.0.1