# Soda-streaming
repository contains:
- [data-monitor](/data-monitor/README.md)
- [poc-tooling](/poc-tooling/README.md)

## Proof of Concept

### Phase 1

### Setup
Start the docker-compose environment.

This contains:
- kafka zookeeper 
- kafka broker 
- kafka-connect 
- Flink cluster (1 jobmanager, 1 taskmanager)

```
cd poc-tooling
docker-compose up -d
```
Wait until connect is `Up (healthy)`
```
watch -n 3 docker-compose ps
```
alternatively, you can check [localhost:8083](http://localhost:8083)

Start the datagenerator:
```
curl -i -X POST -H Accept:application/json -H Content-Type:application/json http://localhost:8083/connectors/ -d @kafka-connect-datagen/config/stream1.json
```
You should get a HTTP-201 Created response.

Check the data on kafka:
```
docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic stream1 --bootstrap-server localhost:9092
```
This should show messages as `Struct{key=key-xx, value=xxx}` if the data generator is running correctly.

Build the flink job:
```
cd ../data-monitor
mvn clean package
```

Submit the job to the Flink cluster, and inspect the taskmanager logs for the output.
The job will calculate the amount of messages on the stream for a tumbling 5sec window, and print out the result.
```
cd ../poc-tooling
./submit-job.sh
```
You can also check the [Flink UI](http://localhost:8081)