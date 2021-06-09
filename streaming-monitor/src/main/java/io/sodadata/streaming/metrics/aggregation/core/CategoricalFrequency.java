/*
 * Copyright 2021 Soda.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sodadata.streaming.metrics.aggregation.core;

import io.sodadata.streaming.metrics.aggregation.ColumnAggregationMetric;
import io.sodadata.streaming.types.Categorical;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoricalFrequency extends ColumnAggregationMetric<Categorical, Map<String, Double>, CategoricalFrequency> {

    private final Map<String, Integer> counts = new HashMap<>();

    public CategoricalFrequency() {
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