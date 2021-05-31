package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class StringLengthAverage extends ColumnAggregationMetric<CharSequence, Double, StringLengthAverage>{

    private int lengthSum = 0;
    private int count = 0;

    protected StringLengthAverage() {
        super("string-length-avg",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthSum += input.length();
        count += 1;
    }

    @Override
    public void merge(StringLengthAverage other) {
        lengthSum += other.lengthSum;
        count += other.count;
    }

    @Override
    public Double getResult() {
        if (count == 0){
            return 0.0;
        }
        return lengthSum/ (double) count;
    }

    @Override
    protected StringLengthAverage create() {
        return new StringLengthAverage();
    }
}
