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

public class NumberAverage extends ColumnAggregationMetric<Number, Double, NumberAverage> {

    private double sum = 0;
    private int count = 0;


    public NumberAverage() {
        super("number-avg", Number.class);
    }

    @Override
    public void add(Number input) {
        sum += input.doubleValue();
        count += 1;
    }

    @Override
    public void merge(NumberAverage other) {
        sum += other.sum;
        count += other.count;
    }

    @Override
    public Double getResult() {
        if (count == 0){
            return 0.0;
        }
        return sum/ (double) count;
    }

    @Override
    protected NumberAverage create() {
        return new NumberAverage();
    }

}
