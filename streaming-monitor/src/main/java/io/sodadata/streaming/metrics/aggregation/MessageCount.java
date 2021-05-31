package io.sodadata.streaming.metrics.aggregation;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

public class MessageCount extends BaseAggregationMetric<GenericRecord, Integer, MessageCount> {

    private Integer accumulator = 0;

    protected MessageCount() {
        super("message-count");
    }

    @Override
    public void add(GenericRecord input) {
        accumulator += 1;
    }

    @Override
    public void merge(MessageCount other) {
        accumulator += other.accumulator;
    }

    @Override
    public Integer getResult() {
        return accumulator;
    }

    @Override
    protected MessageCount create() {
        return new MessageCount();
    }
}
