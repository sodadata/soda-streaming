package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NullCount extends ColumnAggregationMetric<Object, Integer, NullCount> {

    private Integer count = 0 ;

    protected NullCount() {
        super("nul-count", Object.class);
    }

    @Override
    public void add(Object input) {
        //TODO: this if statement is unneeded given the current accepts implementation
        if (input == null) {
            count += 1;
        }
    }

    @Override
    public void merge(NullCount other) {
        count += other.count;
    }

    @Override
    public Integer getResult() {
        return count;
    }

    @Override
    protected NullCount create() {
        return new NullCount();
    }

    @Override
    public boolean accepts(Object in) {
        return in == null;
    }
}