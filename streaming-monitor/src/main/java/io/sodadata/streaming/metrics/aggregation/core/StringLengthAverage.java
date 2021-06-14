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

public class StringLengthAverage extends ColumnAggregationMetric<CharSequence, Double, StringLengthAverage> {

    private int lengthSum = 0;
    private int count = 0;

    public StringLengthAverage() {
        super("string_length_avg",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthSum += input.length();
        count += 1;
    }

    @Override
    public void merge(StringLengthAverage other) {
        lengthSum += other.lengthSum;
        count += other.count;
    }

    @Override
    public Double getResult() {
        if (count == 0){
            return 0.0;
        }
        return lengthSum/ (double) count;
    }

    @Override
    protected StringLengthAverage create() {
        return new StringLengthAverage();
    }
}
