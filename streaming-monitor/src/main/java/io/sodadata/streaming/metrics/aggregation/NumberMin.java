package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class NumberMin extends ColumnAggregationMetric<Number, Double, NumberMin>{

    private Double min;


    protected NumberMin() {
        super("number-min",Number.class);
    }

    @Override
    public void add(Number input) {
        min = nullSafeMin(min,input.doubleValue());
    }

    @Override
    public void merge(NumberMin other) {
        min = nullSafeMin(min,other.min);
    }

    @Override
    public Double getResult() {
        if (min == null){
            // TODO: decide if -1 is correct null value.. (alternative is Integer.MAX)
            return -1.0;
        }
        return min;
    }

    @Override
    protected NumberMin create() {
        return new NumberMin();
    }

    private Double nullSafeMin(Double a, Double b){
        if (a == null & b == null) { return null;}
        else if (a == null ) { return b;}
        else if (b == null ) { return a;}
        else return Math.min(a,b);
    }
}
