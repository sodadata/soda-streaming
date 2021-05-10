# data-monitor

Project setup initialized with [flink maven quickstart](https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/dev/datastream/project-configuration/#maven-quickstart):

### Running in IDE

The main flink job is located in `soda.streaming.StreamingJob`

You can setup a run configuration in IntelliJ with the following configuration:
- Java 11 runtime
- Main class: `soda.streaming.StreamingJob`
- Program arguments: `"localhost:9092"` (This is the kafka endpoint that will be used)
- modify options > include dependencies with "Provided" scope

### Build
To build the Jar:
`mvn clean package`

### versions
- Flink `v1.13.0`