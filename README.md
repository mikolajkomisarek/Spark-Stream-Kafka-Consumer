# What is Spark Kafka Consumer Service?


# How to use this image

```console
$ docker run --name some-spark-kafka-consumer-service --network some-network -d mikolajkomisarek/spark-kafka-consumer-service
```

The following environment variables are also honored for configuring your Spark Kafka Consumer Service instance:

-	`-e BOOTSTRAP_SERVER_URL=...`
-	`-e SCHEMA_REGISTRY_URL=...`
-	`-e OFFSET_RESET=...`
-	`-e GROUP_ID=...`
-	`-e TOPIC_PROCESSED=...`
-	`-e TOPIC_RAW=...`
-	`-e SPARK_MASTER=`
-	`-e SPARK_APPLICATION_NAME=...`
-	`-e SPARK_STREAM_DURATION=...`


Example `docker-compose.yml` for `Spark Kafka Consumer Service`:

```yaml
version: '3.1'

services:

  spark-stream-kafka-consumer:
    image: mikolajkomisarek/spark-kafka-consumer-service
    depends_on:
      - broker
      - zookeeper
      - schema-registry
    environment:
      - BOOTSTRAP_SERVER_URL=http://localhost:9092
      - SCHEMA_REGISTRY_URL=http://localhost:8081
      - OFFSET_RESET=latest
      - GROUP_ID=group-test
      - TOPIC_PROCESSED=netflow-processed
      - TOPIC_RAW=netflow-raw
      - SPARK_MASTER=local[*]
      - SPARK_APPLICATION_NAME=spark-stream-kafka-consumer
      - SPARK_STREAM_DURATION=1
```

