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

public class NumberMin extends ColumnAggregationMetric<Number, Double, NumberMin> {

    private Double min;


    public NumberMin() {
        super("number-min",Number.class);
    }

    @Override
    public void add(Number input) {
        min = nullSafeMin(min,input.doubleValue());
    }

    @Override
    public void merge(NumberMin other) {
        min = nullSafeMin(min,other.min);
    }

    @Override
    public Double getResult() {
        if (min == null){
            // TODO: decide if -1 is correct null value.. (alternative is Integer.MAX)
            return -1.0;
        }
        return min;
    }

    @Override
    protected NumberMin create() {
        return new NumberMin();
    }

    private Double nullSafeMin(Double a, Double b){
        if (a == null & b == null) { return null;}
        else if (a == null ) { return b;}
        else if (b == null ) { return a;}
        else return Math.min(a,b);
    }
}
