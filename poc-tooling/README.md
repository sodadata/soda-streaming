# poc-tooling

Repository containing the tooling for showcasing the end-to-end POC demo

### Start the full test environment:
`docker-compose up --build -d`

This will start:
- kafka zookeeper
- kafka broker
- python data generator
- flink cluster

### Utility scripts:

`e2edemo.sh` <br> Run end-to-end demo pipeline

`build-new-jar.sh` <br> Build a new jar and replace `streaming-monitor.jar`

`submit-job.sh` <br> Submit the `streaming-monitor.jar` to the flink cluster 
### Versions:

- `Kafka 2.7.0`



### Endpoints

Flink UI: http://localhost:8081