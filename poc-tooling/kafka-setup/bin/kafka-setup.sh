#!/bin/bash
echo "Waiting for Kafka to be ready..."
cub kafka-ready -b broker:29092 1 20
kafka-topics --create --if-not-exists --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1 --topic stream1
echo "Waiting 30 seconds for Connect to be ready..."
sleep 30
curl -i -X POST -H Accept:application/json -H Content-Type:application/json http://connect:8083/connectors/ -d @/tmp/connect/stream1.json