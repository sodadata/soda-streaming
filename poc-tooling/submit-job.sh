JM_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp $1 "${JM_CONTAINER}":/job.jar
docker exec -t -i "${JM_CONTAINER}" flink run -d /job.jar