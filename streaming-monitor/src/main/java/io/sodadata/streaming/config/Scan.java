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

package io.sodadata.streaming.config;

import java.util.*;

public class Scan {
    private String stream_name;
    private RegistryType schema;
    private SchemaType schema_type;
    private List<String> metrics;
    private Map<String,List<String>> columns;

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public RegistryType getSchema() {
        return schema;
    }

    public void setSchema(RegistryType schema) {
        this.schema = schema;
    }

    public SchemaType getSchema_type() {
        return schema_type;
    }

    public void setSchema_type(SchemaType schema_type) {
        this.schema_type = schema_type;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public Map<String,List<String>> getColumns() {
        return columns;
    }

    public void setColumns(Map<String,List<String>> columns) {
        this.columns = columns;
    }

    private Map<String,List<String>> getTransposedColumns(){
        Map<String,List<String>> newMap = new HashMap<>();
        for (Map.Entry<String,List<String>> columnMetrics:columns.entrySet()) {
            String col = columnMetrics.getKey();
            for (String metric: columnMetrics.getValue()){
                newMap.putIfAbsent(metric, new ArrayList<>());
                newMap.get(metric).add(col);
            }
        }
        return newMap;
    }

    public List<MetricConfig> getMetricConfigs(){
        List<MetricConfig> result = new ArrayList<>();
        Map<String,List<String>> metricColumnsMap = getTransposedColumns();
        Set<String> metricSet = new HashSet<String>();
        metricSet.addAll(metrics);
        metricSet.addAll(metricColumnsMap.keySet());
        for(String metricName: metricSet) {
            if (metricColumnsMap.containsKey(metricName) && !metrics.contains(metricName)){
                Properties props = new Properties();
                props.put("columns",metricColumnsMap.get(metricName));
                result.add(new MetricConfig(metricName,props));
            } else {
                result.add(new MetricConfig(metricName));
            }
        }
        return result;
    }


    // Only INTERNAL_REGISTRY is supported for now
    public enum RegistryType{
        INTERNAL_REGISTRY,
        ;
//        EXTERNAL_REGISTRY,
//        AUTO
    }

    // Only AVRO is supported for now
    public enum SchemaType{
        AVRO,
//        JSON,
//        PROTOBUF
    }

    @Override
    public String toString() {
        return "Scan{" +
                "stream_name='" + stream_name + '\'' +
                ", schema=" + schema +
                ", schema_type=" + schema_type +
                ", metrics=" + metrics +
                ", columns=" + columns +
                '}';
    }
}
