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
    public AggregationAccumulator merge(AggregationAccumulator aggregationAccumulator, AggregationAccumulator acc1) {
        //TODO: merge accumulators
        return null;
    }
}

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

