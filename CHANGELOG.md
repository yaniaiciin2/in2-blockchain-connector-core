# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- New Context Broker subscription feature that retrieves configs from yaml file.
- New Context Broker subscription feature that implements subscriptions to entities configured.
- New Context Broker notification endpoint that receives all data subscriptions that match with the subscriptions types
- New docker-compose to update the context-broker and the blockchain connector locally
- New additional Spring Boot metadata file for Context Broker YAML configuration attributes
- New test for Context Broker domain classes
- Update README.md with new Context Broker information
- Update CHANGELOG.md with new features

### Deleted
- Delete Blockchain Event Entity because it is not used for now and has its own branch to be implemented in the future.

## [0.0.0] - 2023-07-20
### Added
- Initial project structure (#414)
- .gitignore (#414)
- Dockerfile (#414)
- README.md (#415)
- CHANGELOG.md (#416)
- Azure Pipelines (#419)

[unreleased]:
[0.0.0]: https://dev.azure.com/in2Dome/DOME/_git/in2-dome-blockchain_connector?version=GTv0.0.0
