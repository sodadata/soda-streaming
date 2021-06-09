package io.sodadata.streaming.metrics.aggregation;

import java.io.Serializable;

// The METRIC type parameter is a way to have the merge function limited to the implementing subclass.|
// see https://stackoverflow.com/questions/3284610/returning-an-objects-subclass-with-generics
/**
 * This is the main parent class for aggregation metrics. <br>
 * AggregationMetrics are defined by their input type IN, and their output type OUT. <br>
 * When extending this class you also need to define the implementing class itself as the METRIC type parameter. <br>
 * This is needed to have this type in argument en method signatures for the merge(other) and create() function. <br>
 * For each input record, the add(..) method will be called. <br>
 * After all the inputs for 1 window are passed, the getResult() method will be called to retrieve the metric result.
* */
abstract public class BaseAggregationMetric<IN, OUT, METRIC extends BaseAggregationMetric<IN,OUT,METRIC>> extends AbstractMetric<METRIC> implements Serializable {

    protected BaseAggregationMetric(String name) {
        super(name);
    }

    //This method is called for each input that the metric needs to process.
    //Adapt the internal state of the metric here.
    abstract public void add(IN input);

    //this should be the same subclass as the subclassing class.
    abstract public void merge(METRIC other);

    //retrieve the result after processing all inputs
    abstract public OUT getResult();

    abstract protected METRIC create();
}
