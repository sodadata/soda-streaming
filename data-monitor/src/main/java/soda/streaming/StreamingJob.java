/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package soda.streaming;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.formats.avro.AvroDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import soda.streaming.metrics.AggregationCalculator;
import soda.streaming.metrics.aggregation.AggregationMetricFactory;

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
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		final String broker = (args.length > 0) ? args[0] : "broker:29092" ;
		final String registry = (args.length > 1) ? args[1] : "schema-registry:8081" ;

		final Schema schema = new Schema.Parser()
				.parse(StreamingJob.class.getClassLoader().getResourceAsStream("expedia.avsc"));

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", broker);
		properties.setProperty("group.id", "data-monitor");



		final List<String> metrics = AggregationMetricFactory.getFactory().getRegisteredMetrics();
		final List<String> topics = Arrays.asList("stream1");

		topics.forEach(topic -> {
			FlinkKafkaConsumer<GenericRecord> consumer = new FlinkKafkaConsumer<GenericRecord>(topic, AvroDeserializationSchema.forGeneric(schema), properties);
			consumer.setStartFromLatest();
			DataStream<GenericRecord> stream = env.addSource(consumer);
			DataStream<String> output = stream
					.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
					.aggregate(new AggregationCalculator(metrics));
			output.print();
		});

		// execute program
		env.execute("data-monitor");
	}
}
