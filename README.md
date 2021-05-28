# Soda-streaming
repository contains:
- [data-monitor](/data-monitor/README.md)
- [poc-tooling](/poc-tooling/README.md)

## Proof of Concept

### Phase 2

### Setup
0. You can choose for this setup to run end-to-end (E2E) automatically running the `e2edemo.sh`.
```
e2edemo.sh
```

If you want to go through the steps one by one, follow the steps below:

1. Start the docker-compose environment.

    This contains:
    - kafka zookeeper 
    - kafka broker 
    - Flink cluster (1 jobmanager, 1 taskmanager)
    - Data generator
    
    ```
    cd poc-tooling
    docker-compose up -d
    ```

2. Check the data on kafka:

    ```
    docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic stream1 --bootstrap-server localhost:9092
    ```
    This should show messages as `Struct{key=key-xx, value=xxx}` if the data generator is running correctly.

3. Build a specific flink job:

    (This requires Java 11 or higher)
    ```
    cd ../data-monitor
    mvn clean package
    ```

4. Submit the job to the Flink cluster, and inspect the taskmanager logs for the output.
The job will calculate the amount of messages on the stream for a tumbling 5sec window, and print out the result.
    
   ```
    cd ../poc-tooling
    ./submit-job.sh
    ```
    You can also check the [Flink UI](http://localhost:8081)

5. Stop and clear this POC:
    
    ```
    docker-compose down
    ```
    All docker containers and networks will be stopped and removed. You can validate this by running `docker ps`.