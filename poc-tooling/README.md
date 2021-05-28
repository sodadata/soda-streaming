# poc-tooling

Repository containing the tooling for showcasing the end-to-end POC demo

### Start the full test environment:
`docker-compose up -d`

This will start:
- kafka zookeeper
- kafka broker
- kafka connect service


#### Data-generators

The datagenerators are based on https://github.com/confluentinc/kafka-connect-datagen.

There are multiple data generators setup in the project, to publish auto generated messages on kafka.
The config needs to be added in 4 places:
- a .avro schema declaring how to generate the data in kafka-connect-datagen/schema
- a .json config file in declaring the configuration settings for the datagenerator in kafka-connect-datagen/config

you can start a demo datagenerator with:
```
curl -i -X PUT -H Accept:application/json -H Content-Type:application/json http://localhost:8083/connectors/datagen-stream1/config -d @kafka-connect-datagen/config/stream1.json
```

once submitted, you can restart it if needed with:

```
curl -i -X POST http://localhost:8083/connectors/datagen-stream1/tasks/0/restart
```

### Versions:

- `Confluent Platform 6.1.1` 
- `Kafka 2.7.0`
- `Kafka Connect Datagen 0.4.0`



### Endpoints

Flink UI: http://localhost:8081
Kafka Connect endpoint: http://localhost:8083