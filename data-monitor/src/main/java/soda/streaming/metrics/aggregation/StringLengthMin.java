package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;

public class StringLengthMin extends ColumnAggregationMetric<CharSequence, Integer, StringLengthMin>{

    private Integer lengthMin = null;

    protected StringLengthMin() {
        super("string-length-min",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthMin = nullSafeMin(lengthMin,input.length());
    }

    @Override
    public void merge(StringLengthMin other) {
        lengthMin = nullSafeMin(this.lengthMin, other.lengthMin);
    }

    private Integer nullSafeMin(Integer a, Integer b){
        if (a == null & b == null) { return null;}
        else if (a == null ) { return b;}
        else if (b == null ) { return a;}
        else return Math.min(a,b);
    }

    @Override
    public Integer getResult() {
        if (lengthMin == null){
            // TODO: decide if -1 is correct null value.. (alternative is Integer.MAX)
            return -1;
        }
        return lengthMin;
    }

    @Override
    protected StringLengthMin create() {
        return new StringLengthMin();
    }
}
