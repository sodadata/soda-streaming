package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;

/**
 * An abstract metric class representing a metric that has to be applied on a column. <br>
 * This means the input type of the metric is not the whole record, but only the type of the column that it calculates a metric over.
 * This class should only hold the metric state for 1 column.
 * So if you have a String ColumnAggregationMetric that needs to be applied to 3 String columns,
 * you will have 3 instances of this ColumnAggregationMetric each holding the state for their respective column. <br>
 *
 * This class extends the BaseAggregationMetric by also implementing an accepts(in) function that can be used as a filter.
 * The main implementation is to check if the input object is of the correct input Type.
 * This can be overridden by implementing classes to have a more custom filtering.
 * */
abstract public class ColumnAggregationMetric<IN, OUT, METRIC extends ColumnAggregationMetric<IN, OUT, METRIC>>
        extends BaseAggregationMetric<IN,OUT,METRIC>{

    private final Class<IN> inputClazz;

    protected ColumnAggregationMetric(String name,Class<IN> inputClazz) {
        super(name);
        this.inputClazz = inputClazz;
    }

    public boolean accepts(Object in){
        return this.inputClazz.isInstance(in);
    }
}
