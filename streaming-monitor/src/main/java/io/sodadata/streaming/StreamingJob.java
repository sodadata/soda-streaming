/*
 * Copyright 2021 Soda.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sodadata.streaming;

import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.formats.avro.AvroDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import io.sodadata.streaming.config.Parser;
import io.sodadata.streaming.config.Warehouse;
import io.sodadata.streaming.metrics.AggregationCalculator;
import io.sodadata.streaming.metrics.AggregationWindowOutput;
import io.sodadata.streaming.metrics.aggregation.AggregationMetricFactory;

import java.io.IOException;
import java.util.*;

/**
 * The main stream monitoring job.
 */
public class StreamingJob {

	public static void main(String[] args) throws Exception {
		// read in the warehouse file, the file path can be passed as program arg.
		final String warehouse_config_path = (args.length > 0) ? args[0] : "datasource_cluster.yml";
		final Warehouse warehouse = Parser.parseWarehouseFile(warehouse_config_path);
		System.out.printf("Read in warehouse file: \n %s%n", warehouse);

		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// set up the kafka connection properties
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", warehouse.getConnection().getURL());
		properties.setProperty("group.id", "data-monitor");

		// define the list of metrics to calculate, and the topics to monitor
		final List<String> metrics = AggregationMetricFactory.getFactory().getRegisteredMetrics();
		final List<String> topics = Arrays.asList("travel","food");

		// setup a flink pipeline to monitor each topic
		topics.forEach(topic -> {
			try {
				// 1. parse the schema, for each topic we expect a <topic>.avsc file to be present in the schema-registry dir.
				Schema schema = new Schema.Parser()
						.parse(StreamingJob.class.getClassLoader().getResourceAsStream(String.format("schema-registry/%s.avsc",topic )));

				// 2. set up the flink source for the topic
				FlinkKafkaConsumer<GenericRecord> consumer = new FlinkKafkaConsumer<>(topic, AvroDeserializationSchema.forGeneric(schema), properties);
				consumer.setStartFromLatest();
				DataStream<GenericRecord> stream = env.addSource(consumer);

				// 3. add the metric calculation pipeline to the source
				DataStream<String> output = stream
						.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)))
						.aggregate(new AggregationCalculator(metrics), new AggregationWindowOutput(topic));

				// 4. print the output
				output.print();
			} catch (IOException | SchemaParseException e) {
				System.out.printf("ERROR: could not read/find schema file for %s%n",topic);
				System.out.println(e.toString());
				//TODO: should we abort the whole program, or execute with the streams that are valid.
			}
		});

		// Start the full flink job
		env.execute("data-monitor");
	}
}
