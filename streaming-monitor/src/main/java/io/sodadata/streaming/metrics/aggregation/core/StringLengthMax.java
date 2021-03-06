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

public class StringLengthMax extends ColumnAggregationMetric<CharSequence, Integer, StringLengthMax> {

    private Integer lengthMax = 0;

    public StringLengthMax() {
        super("string_length_max",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthMax = Math.max(lengthMax,input.length());
    }

    @Override
    public void merge(StringLengthMax other) {
        lengthMax = Math.max(this.lengthMax, other.lengthMax);
    }

    @Override
    public Integer getResult() {
        return lengthMax;
    }

    @Override
    protected StringLengthMax create() {
        return new StringLengthMax();
    }

}
