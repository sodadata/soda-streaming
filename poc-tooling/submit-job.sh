JM_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp ../streaming-monitor/target/streaming-monitor-0.0.1-SNAPSHOT.jar "${JM_CONTAINER}":/job.jar
docker exec -t -i "${JM_CONTAINER}" flink run -d /job.jar
docker-compose logs -f taskmanager