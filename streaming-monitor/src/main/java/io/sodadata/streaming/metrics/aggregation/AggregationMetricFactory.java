package io.sodadata.streaming.metrics.aggregation;

import io.sodadata.streaming.metrics.aggregation.core.*;
import org.apache.avro.generic.GenericRecord;

import java.util.*;


/**
 * Factory class to create new instances of a specific metric. <br>
 * This is mainly used in the AggregationAccumulator.
 */
public class AggregationMetricFactory {
    // Internal state of the factory: a register of al the available metrics
    // (metric-name -> metric-instance)
    private static final Map<String, BaseAggregationMetric<GenericRecord, ?, ?>> metricMap = new HashMap<>();

    // Singleton factory object
    private static final AggregationMetricFactory factory = new AggregationMetricFactory();


    // Private constructor prevents instantiation from other classes, outside classes shoud use getFactory() to retrieve the singleton factory
    private AggregationMetricFactory() {
        // List of all core metrics
        // Add newly implemented metrics here!
        final List<BaseAggregationMetric<GenericRecord, ?, ?>> bundledMetrics = Arrays.asList(
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

        for (BaseAggregationMetric<GenericRecord, ?, ?> metric : bundledMetrics) {
            this.register(metric);
        }
    }

    //retrieve the singleton factory object
    public static AggregationMetricFactory getFactory() {
        return factory;
    }

    //register a new BaseAggregation metric to the factory
    public void register(BaseAggregationMetric<GenericRecord, ?, ?> metric) {
        metricMap.put(metric.name, metric);
    }

    //factory method: create a metric from the list of registered metrics
    public BaseAggregationMetric<GenericRecord, ?, ?> createMetric(String metric) {
        if (metricMap.containsKey(metric)) {
            return metricMap.get(metric).create();
        } else {
            throw new IllegalArgumentException(String.format("Count not find aggregation metric '%s' in registered metrics: %s", metric, this.getRegisteredMetrics()));
        }
    }

    //get a list of all the registered metrics in the factory
    public List<String> getRegisteredMetrics() {
        return new ArrayList<>(metricMap.keySet());
    }
}