package io.sodadata.streaming.metrics;

import io.sodadata.streaming.config.MetricConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichAggregateFunction;
import org.apache.flink.streaming.runtime.operators.windowing.KeyMap;
import io.sodadata.streaming.Utils;
import io.sodadata.streaming.metrics.aggregation.AggregationMetricFactory;
import io.sodadata.streaming.metrics.aggregation.BaseAggregationMetric;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Main Flink AggregateFunction that is used to calculate all metric outputs. <br>
 * It can be instantiated with a list of metric (by name) that you want in the output. <br>
 * It takes GenericRecord (avro specific) as input; <br>
 * Holds an AggregationAccumulator as state; <br>
 * Outputs a Map(String -> BaseAggregationMetric), keyed by metric name;
 * */
public class AggregationCalculator implements AggregateFunction<GenericRecord, AggregationAccumulator, Map<String, BaseAggregationMetric<GenericRecord,?,?>>> {

    private final List<MetricConfig> metrics;

    public AggregationCalculator(List<MetricConfig> metrics) {
        this.metrics = metrics;
    }

    @Override
    public AggregationAccumulator createAccumulator() {
        return new AggregationAccumulator(this.metrics);
    }

    @Override
    public AggregationAccumulator add(GenericRecord s, AggregationAccumulator acc) {
        acc.add(s);
        return acc;
    }

    @Override
    public Map<String, BaseAggregationMetric<GenericRecord,?,?>>  getResult(AggregationAccumulator acc) {
        return acc.getResult();
    }

    @Override
    public AggregationAccumulator merge(AggregationAccumulator acc0, AggregationAccumulator acc1) {
        //this has yet to be implemented, but it's not needed until we use parallelism
        AggregationAccumulator acc = new AggregationAccumulator(this.metrics);
        for (String metric: acc.metrics.keySet()){
            //TODO: Find solution to do this with the types.
//            var base = (BaseAggregationMetric<GenericRecord,?,BaseAggregationMetric<GenericRecord,?,BaseAggregationMetric<GenericRecord,?,?>>>) acc.metrics.get(metric);
//            base.merge(acc0.metrics.get(metric));
//            base.merge(acc1.metrics.get(metric));
        }
        return acc0;
    }
}

/**
 * Internal mutable state object for the AggregationCalculator.
 * It internally holds a Map(String -> BaseAggregationMetric) and pushes each incoming record to all the BaseAggregationMetrics in the map.
 * All these BaseAggregationMetrics are then updating their state accordingly.
 * On output it will return this map.
 * */
class AggregationAccumulator {
    protected final Map<String, BaseAggregationMetric<GenericRecord,?,?>> metrics;

    private final AggregationMetricFactory factory = AggregationMetricFactory.getFactory();

    AggregationAccumulator(List<MetricConfig> metricConfigs) {
        metrics = metricConfigs.stream()
                .collect(Collectors.toMap(MetricConfig::getName, x -> factory.createMetric(x.getName(),x.getConfig())));
    }
    public void add(GenericRecord s){
        for (BaseAggregationMetric<GenericRecord,?,?> metric : metrics.values()){
            metric.add(s);
        }
    }

    public Map<String, BaseAggregationMetric<GenericRecord,?,?>> getResult(){
        return metrics;
    }
}

