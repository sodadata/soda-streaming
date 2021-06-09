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

public class NumberMax extends ColumnAggregationMetric<Number, Double, NumberMax> {

    private Double max = 0.0;


    public NumberMax() {
        super("number-max",Number.class);
    }

    @Override
    public void add(Number input) {
        max = Math.max(max,input.doubleValue());
    }

    @Override
    public void merge(NumberMax other) {
        max = Math.max(max,other.max);
    }

    @Override
    public Double getResult() {
        return max;
    }

    @Override
    protected NumberMax create() {
        return new NumberMax();
    }

}
