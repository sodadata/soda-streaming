package io.sodadata.streaming.metrics.aggregation;

import io.sodadata.streaming.types.Categorical;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoricalFrequency extends ColumnAggregationMetric<Categorical, Map<String, Double>, CategoricalFrequency> {

    private final Map<String, Integer> counts = new HashMap<>();

    protected CategoricalFrequency() {
        super("categorical-frequency", Categorical.class);
    }

    @Override
    public void add(Categorical input) {
        String value = input.getValue();
        if (!counts.containsKey(value)) {
            counts.put(value, 0);
        }
        counts.replace(value, counts.get(value) + 1);
    }

    @Override
    public void merge(CategoricalFrequency other) {
        //Merge maps by key
        for (String key : other.counts.keySet()) {
            this.counts.put(key, counts.getOrDefault(key, 0) + other.counts.get(key));
        }
    }

    @Override
    public Map<String, Double> getResult() {
        Integer sum = counts.values().stream().reduce(0, Integer::sum);
        return counts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / (double) sum));
    }

    @Override
    protected CategoricalFrequency create() {
        return new CategoricalFrequency();
    }

}