package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class StringLengthMax extends ColumnAggregationMetric<CharSequence, Integer, StringLengthMax>{

    private Integer lengthMax = 0;

    protected StringLengthMax() {
        super("string-length-max",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthMax = Math.max(lengthMax,input.length());
    }

    @Override
    public void merge(StringLengthMax other) {
        lengthMax = Math.max(this.lengthMax, other.lengthMax);
    }

    @Override
    public Integer getResult() {
        return lengthMax;
    }

    @Override
    protected StringLengthMax create() {
        return new StringLengthMax();
    }

}
