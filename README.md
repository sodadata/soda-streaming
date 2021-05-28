# Soda-streaming
repository contains:
- [data-monitor](/data-monitor/README.md)
- [poc-tooling](/poc-tooling/README.md)

## Proof of Concept

### Phase 1

### Setup
0. You can choose for this setup to run end-to-end (E2E) automatically running the `e2edemo.sh`.  
Two setups can be chosen based on the type of data one wants. Use the flag `-f` to specify the type of data you want
to use.
The flag options are: `avro-data` OR `flat-data`.  
Example Usage:
```
e2edemo.sh -f avro-data
```
Note, you need at least docker-compose v1.29 (to install latest version see [here](https://docs.docker.com/compose/install/))

If you want to go through the steps one by one, follow the steps below:

1. Start the docker-compose environment.

    This contains:
    - kafka zookeeper 
    - kafka broker 
    - kafka-connect 
    - Flink cluster (1 jobmanager, 1 taskmanager)
    
    ```
    cd poc-tooling
    docker-compose up -d
    ```

2. Wait until kafka-connect is up and running. Check this by  

    ```
    watch -n 3 docker-compose ps
    ```
    and check `Connect ` has State **Up (healthy)**
    
    OR
    
    check [localhost:8083](http://localhost:8083) for details on the kafka-connect component.
    Details on *version*, *commit* and *kafka_cluster_id* should be visible.  

3. Start a datagenerator: 

    Two types of data are developed: one through 'kafka-connect' and one through a custom made component 'avrotokafkadatagenerator'
    1. KAFKA-CONNECT: The datagenerators are based on https://github.com/confluentinc/kafka-connect-datagen.

        Datagenerator is based on this [confluence-project](https://github.com/confluentinc/kafka-connect-datagen). The core
        principle of the data generation is based on this [avro-random-generator](https://github.com/confluentinc/avro-random-generator)
        This boils down to kafka-connect with some pre-installed libraries such that a kafka-connect connector is available for
        configuration. This connector generates mock data and pushes this to Kafka.
        Setup this connector by:  
        ```
        curl -i -X PUT -H Accept:application/json -H Content-Type:application/json http://localhost:8083/connectors/datagen-stream1/config -d @kafka-connect-datagen/config/stream1.json
        ```
        You should get a HTTP-201 Created response. Using the REST API endpoint at localhost:8083/connectors one can see the
        connector is added.  
        
    2. Custom made component 'avrotokafkadatagenerator', if chosen this option, the datagenerator will start automatically.

4. Check the data on kafka:

    ```
    docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic stream1 --bootstrap-server localhost:9092
    ```
    This should show messages as `Struct{key=key-xx, value=xxx}` if the data generator is running correctly.

5. Build a specific flink job:

    (This requires Java 11 or higher)
    ```
    cd ../data-monitor
    mvn clean package
    ```

6. Submit the job to the Flink cluster, and inspect the taskmanager logs for the output.
The job will calculate the amount of messages on the stream for a tumbling 5sec window, and print out the result.
    
   ```
    cd ../poc-tooling
    ./submit-job.sh
    ```
    You can also check the [Flink UI](http://localhost:8081)

7. Stop and clear this POC:
    
    ```
    docker-compose down
    ```
    All docker containers and networks will be stopped and removed. You can validate this by running `docker ps`.