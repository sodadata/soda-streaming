package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class NumberMax extends ColumnAggregationMetric<Number, Double, NumberMax>{

    private Double max = 0.0;


    protected NumberMax() {
        super("number-max",Number.class);
    }

    @Override
    public void add(Number input) {
        max = Math.max(max,input.doubleValue());
    }

    @Override
    public void merge(NumberMax other) {
        max = Math.max(max,other.max);
    }

    @Override
    public Double getResult() {
        return max;
    }

    @Override
    protected NumberMax create() {
        return new NumberMax();
    }

}
