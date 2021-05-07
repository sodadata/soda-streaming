# soda-streaming-poc-tooling

Repository containing the tooling for showcasing the end-to-end POC demo

### Start the full test environment:
`docker-compose up -d`

This will start:
- kafka zookeeper
- kafka broker
- kafka connect service
- schema registry
- confluent control center

- a temporary kafka container for topic creation and configuration


#### Data-generators

The datagenerators are based on https://github.com/confluentinc/kafka-connect-datagen.
The datagen repo is contained in this repo as a submodule in `/kafka-connect-datagen/build`.

There are multiple data generators setup in the project, to publish auto generated messages on kafka.
The config needs to be added in 4 places:
- a .avro schema declaring how to generate the data in kafka-connect-datagen/config
- a config file in kafka-setup declaring the configuration settings for the datagenerator
- a line in the entrypoint script in kafka-setup/bin to make a new topic
- a line in the entrypoint script in kafka-setup/bin to submit the datagen config.

### Versions:

- `Confluent Platform 6.1.1` 
- `Kafka 2.7.0`
- `Kafka Connect Datagen 0.4.0`



### Endpoints

Conlfuent Control center UI at port 9021