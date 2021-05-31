package soda.streaming.metrics.aggregation;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import soda.streaming.formats.avro.AvroTypeConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: a lot of this logic is avro specific, in later iterations this should be abstracted out
public class ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT ,
            BASE_METRIC extends ColumnAggregationMetric<BASE_IN,BASE_OUT,BASE_METRIC>>
        extends BaseAggregationMetric<GenericRecord, Map<String,BASE_OUT>, ColumnAggregationMetricAggregator<BASE_IN, BASE_OUT,BASE_METRIC>>
{

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