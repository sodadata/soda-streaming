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

public class NullCount extends ColumnAggregationMetric<Object, Integer, NullCount> {

    private Integer count = 0 ;

    public NullCount() {
        super("null-count", Object.class);
    }

    @Override
    public void add(Object input) {
        //TODO: this if statement is unneeded given the current accepts implementation
        if (input == null) {
            count += 1;
        }
    }

    @Override
    public void merge(NullCount other) {
        count += other.count;
    }

    @Override
    public Integer getResult() {
        return count;
    }

    @Override
    protected NullCount create() {
        return new NullCount();
    }

    @Override
    public boolean accepts(Object in) {
        return in == null;
    }
}