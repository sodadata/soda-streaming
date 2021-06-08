package io.sodadata.streaming.metrics.aggregation;

import io.sodadata.streaming.metrics.AbstractMetric;

import java.io.Serializable;
import java.util.Properties;

// The METRIC type parameter is a way to have the merge function limited to the implementing subclass.|
// see https://stackoverflow.com/questions/3284610/returning-an-objects-subclass-with-generics
abstract public class BaseAggregationMetric<IN, OUT, METRIC extends BaseAggregationMetric<IN,OUT,METRIC>> extends AbstractMetric<METRIC> implements Serializable {

    protected BaseAggregationMetric(String name) {
        super(name);
    }

    abstract public void add(IN input);

    //this should be the same subclass as the subclassing class.
    abstract public void merge(METRIC other);

    abstract public OUT getResult();

    @Override
    abstract protected METRIC create();

    protected METRIC create(Properties config){
        return this.create();
    };
}
