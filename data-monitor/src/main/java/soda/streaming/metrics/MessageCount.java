package soda.streaming.metrics;

import org.apache.flink.api.common.functions.AggregateFunction;

public class MessageCount implements AggregateFunction<String, Integer, Integer> {
    @Override
    public Integer createAccumulator() {
        return 0;
    }

    @Override
    public Integer add(String s, Integer integer) {
        return integer + 1;
    }

    @Override
    public Integer getResult(Integer integer) {
        return integer;
    }

    @Override
    public Integer merge(Integer integer, Integer acc1) {
        return integer + acc1;
    }
}
