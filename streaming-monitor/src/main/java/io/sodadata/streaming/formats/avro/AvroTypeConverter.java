package io.sodadata.streaming.formats.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import io.sodadata.streaming.metrics.aggregation.CategoricalFrequency;
import io.sodadata.streaming.types.Categorical;

import java.util.List;

//Currently only used for enum type conversion to a wrapper Categorical class
public class AvroTypeConverter {
    static public Object convert(Object in){
        if (in instanceof GenericData.EnumSymbol) return new Categorical(in.toString());
        return in;
    }
}