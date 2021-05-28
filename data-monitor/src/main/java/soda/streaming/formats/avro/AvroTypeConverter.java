package soda.streaming.formats.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import soda.streaming.metrics.aggregation.CategoricalFrequency;
import soda.streaming.types.Categorical;

import java.util.List;

//Currently only used for enum type conversion to a wrapper Categorical class
public class AvroTypeConverter {
    static public Object convert(Object in){
        if (in instanceof GenericData.EnumSymbol) return new Categorical(in.toString());
        return in;
    }
}
