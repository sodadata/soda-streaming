package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;
import org.apache.flink.shaded.jackson2.org.yaml.snakeyaml.events.MappingEndEvent;
import soda.streaming.metrics.AbstractMetric;

import java.io.Serializable;

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

    abstract protected METRIC create();
}
