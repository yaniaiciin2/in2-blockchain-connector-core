# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- New Context Broker subscription feature that retrieves configs from yaml file. (#421)
- New Context Broker subscription feature that implements subscriptions to entities configured.(#421)
- New Context Broker notification endpoint that receives all data subscriptions that match with the subscriptions types. (#421)
- New docker-compose to update the context-broker and the blockchain connector locally(#421)
- New additional Spring Boot metadata file for Context Broker YAML configuration attributes(#421)
- New test for Context Broker domain classes(#421)
- New Subscription Service to implement subscription business logic.
### Change
- Changed Context Broker Config to use Subs Service
- Update README.md with new Context Broker information (#421)
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
