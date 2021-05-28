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

package soda.streaming.metrics;

import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import soda.streaming.Utils;
import soda.streaming.metrics.aggregation.BaseAggregationMetric;

import java.util.Date;
import java.util.Map;

public class AggregationWindowOutput extends ProcessAllWindowFunction<Map<String, BaseAggregationMetric<GenericRecord,?,?>>,String, TimeWindow> {
    final String topic;

    public AggregationWindowOutput(String topic) {
        this.topic = topic;
    }

    @Override
    public void process(Context context, Iterable<Map<String, BaseAggregationMetric<GenericRecord, ?, ?>>> iterable, Collector<String> collector) throws Exception {
        Map<String, BaseAggregationMetric<GenericRecord, ?, ?>> metrics = iterable.iterator().next();
        Date timestamp = new Date(context.window().getEnd());
        Date nextScan = new Date(2 * context.window().getEnd() - context.window().getStart());
        String result = Utils.formatAggregatorOutput(metrics,timestamp.toString(),this.topic,nextScan.toString());
        collector.collect(result);
    }
}
