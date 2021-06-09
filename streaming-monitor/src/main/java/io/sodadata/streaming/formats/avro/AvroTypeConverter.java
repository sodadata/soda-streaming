package io.sodadata.streaming.formats.avro;

import org.apache.avro.generic.GenericData;
import io.sodadata.streaming.types.Categorical;

//Currently only used for enum type conversion to a wrapper Categorical class
public class AvroTypeConverter {
    static public Object convert(Object in){
        if (in instanceof GenericData.EnumSymbol) return new Categorical(in.toString());
        return in;
    }
}
