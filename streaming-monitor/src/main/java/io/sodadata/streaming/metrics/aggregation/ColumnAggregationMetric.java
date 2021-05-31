package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;

abstract public class ColumnAggregationMetric<IN, OUT, METRIC extends ColumnAggregationMetric<IN, OUT, METRIC>> extends BaseAggregationMetric<IN,OUT,METRIC>{

    private final Class<IN> inputClazz;

    protected ColumnAggregationMetric(String name,Class<IN> inputClazz) {
        super(name);
        this.inputClazz = inputClazz;
    }

    public boolean accepts(Object in){
        return this.inputClazz.isInstance(in);
    }

}
