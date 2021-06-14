#!/bin/bash


consume_messages_from_kafka_for_limited_time() {
  timeout -s SIGKILL --foreground ${1-2m} docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic travel --bootstrap-server localhost:9092 --max-messages ${2-10}
}

inspect_flink_taskmanager() {
  docker-compose logs --tail=100 -f taskmanager
}

send_trigger_for_partially_invalid_data() {
 curl --header "Content-Type: application/json" --request POST --data '{"action_request": "partially_invalid", "topic_description": "travel"}' http://localhost:5000/api/v0.1/actions
}

update_jar_with_specific_new_scan_file() {
  mkdir -p tmp-e2edemo/scans
  cp streaming-monitor.jar tmp-e2edemo
  cp -r specific-scans/* tmp-e2edemo/scans
  cd tmp-e2edemo
  jar uf streaming-monitor.jar ./scans
  cd ..
}

stop_flink_job() {
  jobid=$(curl localhost:8081/v1/jobs | jq '.jobs[].id'| cut -d '"' -f 2)
  curl -X PATCH localhost:8081/v1/jobs/$jobid
}

remove_temp_files_folders() {
  rm -r tmp-e2edemo
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
./submit-job.sh ./streaming-monitor.jar
echo ""
sleep 7
echo "===="
echo "4. Inspecting logs of the flink job "
echo "..."
inspect_flink_taskmanager
echo "===="
echo "5. Stopping first flink job "
echo "..."
stop_flink_job
echo "===="
echo "6. Creating new JAR with updated scan file for new flink job "
echo "..."
update_jar_with_specific_new_scan_file
echo "===="
echo "7. Submitting second - updated scan file - Flink job to the cluster"
echo "..."
./submit-job.sh ./tmp-e2edemo/streaming-monitor.jar
echo ""
echo "===="
echo "8. Inspecting logs of the flink job"
echo "..."
sleep 7
inspect_flink_taskmanager
echo "===="
echo "9. Send trigger to datagenerator to send partially-invalid data"
echo "..."
send_trigger_for_partially_invalid_data
echo "===="
echo "10. Inspecting logs of the flink job"
echo "..."
sleep 7
inspect_flink_taskmanager
echo "..."
echo "===="
echo "11. Shutting the test environment down"
echo "..."
remove_temp_files_folders
docker-compose down
exit





