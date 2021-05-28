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

package soda.streaming;

import org.apache.avro.generic.GenericRecord;
import soda.streaming.metrics.aggregation.BaseAggregationMetric;
import soda.streaming.metrics.aggregation.ColumnAggregationMetric;
import soda.streaming.metrics.aggregation.ColumnAggregationMetricAggregator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Utils {
    /*
    An example of an output log:
    Next output at: 14:00:00 06/05/2021
            ...
            |  Scan summary ------
            |  timestamp: 14:00:00 06/05/2021
            |  stream_name: stream1
	        |  message_count: 500
            |  missing_count: 2
            |  min(col1): 1
            |  avg(col1): 5
            |  max(col1): 10
            |  min_length(col1): 1
            |  avg_length(col1): 5
            |  max_length(col1): 12
            |  cat_freqs(col1): {"catA": 5, "catB":6, "catC": 10}
	        |  min(col2): 2
            |  .... (continue here for other columns and metrics)
            |  frequent_values

    Next output at: 14:05:00 06/05/2021...
    */
    public static String formatAggregatorOutput(Map<String, BaseAggregationMetric<GenericRecord,?,?>> metricMap,String timestamp, String topic ,String nextTimeStamp){
            StringBuilder out = new StringBuilder();
            out.append("--------- \n");
            out.append("Scan summary --- \n");
            out.append(String.format("  | timestamp: %s\n", timestamp));
            out.append(String.format("  | topic: %s\n", topic));
            // column -> (metric -> value)
            Map<String,Map<String,String>> columnMetrics = new HashMap<>();
            for(Map.Entry<String, BaseAggregationMetric<GenericRecord,?,?>> entry: metricMap.entrySet()){
                String metric = entry.getKey();
                BaseAggregationMetric<GenericRecord, ?, ?> result = entry.getValue();
                if (result instanceof ColumnAggregationMetricAggregator){
                    ((Map<String, Object>) result.getResult()).forEach((k,v) -> {
                        columnMetrics.putIfAbsent(k,new HashMap<>());
                        columnMetrics.get(k).put(metric,v.toString());
                    });
                } else {
                    out.append(String.format("  | %s: %s \n", metric, result.getResult()));
                }
            }
        for (Iterator<String> it = columnMetrics.keySet().stream().sorted().iterator(); it.hasNext(); ) {
            String col = it.next();
            for(Map.Entry<String,String> metricEntry:columnMetrics.get(col).entrySet()){
                String metric = metricEntry.getKey();
                String result = metricEntry.getValue();
                out.append(String.format("  | %s(%s): %s \n", metric, col, result));
            }
        }
            out.append(" ---- \n");
            out.append(String.format("Next output at: %s\n", nextTimeStamp));
            return out.toString();
    }
}
