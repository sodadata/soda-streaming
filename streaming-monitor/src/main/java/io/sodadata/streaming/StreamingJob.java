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
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

	public static void main(String[] args) throws Exception {
		// read in the warehouse file
		final String warehouse_config_path = (args.length > 0) ? args[0] : "datasource_cluster.yml";
		final Warehouse warehouse = Parser.parseWarehouseFile(warehouse_config_path);

		System.out.printf("Read in warehouse file: \n %s%n", warehouse);


		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", warehouse.getConnection().getURL());
		properties.setProperty("group.id", "data-monitor");



		final List<String> metrics = AggregationMetricFactory.getFactory().getRegisteredMetrics();
		final List<String> topics = Arrays.asList("travel","food");

		topics.forEach(topic -> {
			try {
				Schema schema = new Schema.Parser()
						.parse(StreamingJob.class.getClassLoader().getResourceAsStream(String.format("schema-registry/%s.avsc",topic )));
				FlinkKafkaConsumer<GenericRecord> consumer = new FlinkKafkaConsumer<>(topic, AvroDeserializationSchema.forGeneric(schema), properties);
				consumer.setStartFromLatest();
				DataStream<GenericRecord> stream = env.addSource(consumer);
				DataStream<String> output = stream
						.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)))
						.aggregate(new AggregationCalculator(metrics), new AggregationWindowOutput(topic));
				output.print();
			} catch (IOException | SchemaParseException e) {
				System.out.printf("ERROR: could not read/find schema file for %s%n",topic);
				System.out.println(e.toString());
				//TODO: should we abort the whole program, or execute with the streams that are valid.
			}
		});

		// execute program
		env.execute("data-monitor");
	}
}
