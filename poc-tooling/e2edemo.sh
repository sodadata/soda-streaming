#!/bin/bash


consume_messages_from_kafka_for_limited_time() {
  timeout -s SIGKILL --foreground ${1-2m} docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic expedia --bootstrap-server localhost:9092 --max-messages ${2-10}
}

inspect_flink_taskmanager() {
  docker-compose logs --tail=100 -f taskmanager
}

echo "====="
echo " START: END TO END DEMO"
echo "====="
echo "1. Starting docker-compose in background"
echo "..."
echo "building data-generator.."
docker-compose build -q data-generator
docker-compose up -d --remove-orphans
echo ""
sleep 5
echo "====="
echo "2. Inspecting messages on kafka"
echo "..."
consume_messages_from_kafka_for_limited_time
echo ""
echo "====="
echo "3. Submitting Flink job to the cluster"
echo "..."
./submit-job.sh
echo ""
sleep 7
echo "===="
echo "4. Inspecting logs of the flink job "
echo "..."
inspect_flink_taskmanager
echo ""
echo "===="
echo "5. Shutting the test environment down"
echo "..."
docker-compose down
exit



