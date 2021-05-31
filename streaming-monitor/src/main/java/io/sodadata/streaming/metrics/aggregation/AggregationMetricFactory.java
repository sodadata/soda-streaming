package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.generic.GenericRecord;

import java.util.*;
import java.util.stream.Collectors;

public class AggregationMetricFactory {
    private static final List<BaseAggregationMetric<GenericRecord, ?, ?>> bundledMetrics = Arrays.asList(
            new MessageCount(),
            new ColumnAggregationMetricAggregator<>(new StringLengthAverage()),
            new ColumnAggregationMetricAggregator<>(new StringLengthMin()),
            new ColumnAggregationMetricAggregator<>(new StringLengthMax()),
            new ColumnAggregationMetricAggregator<>(new NumberAverage()),
            new ColumnAggregationMetricAggregator<>(new NumberMin()),
            new ColumnAggregationMetricAggregator<>(new NumberMax()),
            new ColumnAggregationMetricAggregator<>(new CategoricalFrequency()),
            new ColumnAggregationMetricAggregator<>(new NullCount())
    );

    // This is where you need to add new Metrics for them to be available
    private static final Map<String, BaseAggregationMetric<GenericRecord, ?, ?>> metricMap = new HashMap<>();

    private static final AggregationMetricFactory factory = new AggregationMetricFactory();

    // Private constructor prevents instantiation from other classes
    private AggregationMetricFactory() {
        for (BaseAggregationMetric<GenericRecord, ?, ?> metric: bundledMetrics){
            this.register(metric);
        }
    }

    public void register(BaseAggregationMetric<GenericRecord, ?, ?> metric){
        metricMap.put(metric.name,metric);
    }

    public static AggregationMetricFactory getFactory() {
        return factory;
    }

    public List<String> getRegisteredMetrics(){ return new ArrayList<>(metricMap.keySet());}

    public BaseAggregationMetric<GenericRecord, ?, ?> createMetric(String metric) {
        if (metricMap.containsKey(metric)) {
            return metricMap.get(metric).create();
        } else {
            throw new IllegalArgumentException(String.format("Count not find aggregation metric '%s' in registered metrics: %s", metric, this.getRegisteredMetrics()));
        }
    }
}