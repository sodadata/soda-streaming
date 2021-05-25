#!/bin/bash

bring_up_whole_infrastructure_with_specific_profile() {
  echo "1. Starting docker-compose in background with profile $1"
  docker-compose --profile $1 up -d
}

wait_until_kafka_connect_healthy() {
  echo "2. Waiting for kafka-connect to come up healthy"
  until [ $(docker inspect -f {{.State.Health.Status}} connect) == "healthy" ]
  do
    echo -e "\tKafka connect NOT 'Up Healthy' yet...waiting..."
    sleep 1s
  done
}

add_datagenerator() {
  HTTP_CODE=$(curl -w '%{http_code}' --silent -X POST -H Accept:application/json -H Content-Type:application/json  http://localhost:8083/connectors/ -d @kafka-connect-datagen/config/stream1.json -o /dev/null)
  if [ $HTTP_CODE == "201" ]
  then
    echo "Datagenerator successfully added to Kafka-Connect"
  else
    echo "Datagenerator NOT added, HTTP_CODE $HTTP_CODE received"
  fi
}

consume_messages_from_kafka_for_limited_time() {
  echo "Consuming messages from kafka"
  timeout -s SIGKILL --foreground ${1-2m} docker-compose exec broker opt/kafka/bin/kafka-console-consumer.sh --topic stream1 --bootstrap-server localhost:9092 --max-messages ${2-10}

}


while getopts "f:" OPTION
do
	case $OPTION in
		f)
			echo "Requested demo to execute with flag: $OPTARG"
			MYOPTF=$OPTARG
			if [ $MYOPTF == "avro-data" ]
			then
			  echo "E2E demo with avro data"
			  bring_up_whole_infrastructure_with_specific_profile "avro-data"
			  sleep 10
			  consume_messages_from_kafka_for_limited_time
			else
			  echo "E2E demo with flat data"
			  bring_up_whole_infrastructure_with_specific_profile "flat-data"
        wait_until_kafka_connect_healthy
        add_datagenerator
        consume_messages_from_kafka_for_limited_time
      fi
      echo "Shutting down infrastructure"
      docker-compose down
			exit
			;;
		\?)
			echo "Used for the help menu"
			exit
			;;
	esac
done



