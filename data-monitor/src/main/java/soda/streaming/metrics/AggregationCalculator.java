package soda.streaming.metrics;

import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichAggregateFunction;
import org.apache.flink.streaming.runtime.operators.windowing.KeyMap;
import soda.streaming.Utils;
import soda.streaming.metrics.aggregation.AggregationMetricFactory;
import soda.streaming.metrics.aggregation.BaseAggregationMetric;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AggregationCalculator implements AggregateFunction<GenericRecord, AggregationAccumulator, Map<String, BaseAggregationMetric<GenericRecord,?,?>>> {

    private final List<String> metrics;

    public AggregationCalculator(List<String> metrics) {
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

    AggregationAccumulator(List<String> metricNames) {
        metrics = metricNames.stream()
                .collect(Collectors.toMap(Function.identity(), factory::createMetric));
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

