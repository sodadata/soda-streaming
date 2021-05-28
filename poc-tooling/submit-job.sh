JM_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp ../data-monitor/target/data-monitor-1.0.0-SNAPSHOT.jar "${JM_CONTAINER}":/job.jar
docker exec -t -i "${JM_CONTAINER}" flink run -d /job.jar