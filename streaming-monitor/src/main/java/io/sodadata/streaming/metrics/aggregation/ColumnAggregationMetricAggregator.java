package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import io.sodadata.streaming.formats.avro.AvroTypeConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class is a BaseAggregationMetric that can be wrapped around a ColumnAggregationMetric subclass to apply that column metric to all applicable columns.
 * Where 1 ColumnAggregationMetric is used to hold the metric state respective to 1 column,
 * this class will hold a ColumnAggregationMetric instance for each of the columns it is applicable to.
 *
 * It will take a GenericRecord as input, extract the values of the columns conforming to the BASE_IN type,
 * and then send these values to the respective ColumnAggregationMetric of that column. <br>
 * This functionality is implemented in the add(input) method and uses the accepts(input) and add(input) methods of the ColumnAggregationMetric.
* */
public class ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT ,
            BASE_METRIC extends ColumnAggregationMetric<BASE_IN,BASE_OUT,BASE_METRIC>>
        extends BaseAggregationMetric<GenericRecord, Map<String,BASE_OUT>, ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT,BASE_METRIC>>
{
    //TODO: a lot of this logic is avro specific, in later iterations this should be abstracted out

    private final BASE_METRIC metricReference;


    private final Map<String,BASE_METRIC> accumulatorMap = new HashMap<>();

    public ColumnAggregationMetricAggregator(BASE_METRIC metric) {
        super(metric.name);
        metricReference = metric;
    }

    @Override
    public void add(GenericRecord input) {
        Schema schema = input.getSchema();
        for (Schema.Field field: schema.getFields()){
            String fieldName = field.name();
            Object fieldValue = AvroTypeConverter.convert(input.get(fieldName));
            if (metricReference.accepts(fieldValue)){
                if (!accumulatorMap.containsKey(fieldName)){
                    accumulatorMap.put(fieldName,metricReference.create());
                }
                try {
                    accumulatorMap.get(fieldName).add((BASE_IN) fieldValue);
                } catch (ClassCastException e) {
                    System.out.println("ERROR: could not cast avro type to expected type");
                    System.out.println("Value: "+input.get(fieldName).toString());
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public void merge(ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT, BASE_METRIC> other) {
        //Merge maps by key
        for (String key: other.accumulatorMap.keySet()){
            // Create an empty metric if the key does not already exist
            if (!this.accumulatorMap.containsKey(key)){
                this.accumulatorMap.put(key,this.metricReference.create());
            }
            // Merge in other metric
            this.accumulatorMap.get(key).merge(other.accumulatorMap.get(key));
        }
    }

    @Override
    public Map<String, BASE_OUT> getResult() {
        return accumulatorMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x-> x.getValue().getResult()));
    }

    @Override
    protected ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT, BASE_METRIC> create() {
        return new ColumnAggregationMetricAggregator<>(this.metricReference);
    }

}
