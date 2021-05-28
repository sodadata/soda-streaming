package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class NumberAverage extends ColumnAggregationMetric<Number, Double, NumberAverage>{

    private double sum = 0;
    private int count = 0;


    protected NumberAverage() {
        super("number-avg", Number.class);
    }

    @Override
    public void add(Number input) {
        sum += input.doubleValue();
        count += 1;
    }

    @Override
    public void merge(NumberAverage other) {
        sum += other.sum;
        count += other.count;
    }

    @Override
    public Double getResult() {
        if (count == 0){
            return 0.0;
        }
        return sum/ (double) count;
    }

    @Override
    protected NumberAverage create() {
        return new NumberAverage();
    }

}
