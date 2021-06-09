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

public class StringLengthMin extends ColumnAggregationMetric<CharSequence, Integer, StringLengthMin> {

    private Integer lengthMin = null;

    public StringLengthMin() {
        super("string_length_min",CharSequence.class);
    }

    @Override
    public void add(CharSequence input) {
        lengthMin = nullSafeMin(lengthMin,input.length());
    }

    @Override
    public void merge(StringLengthMin other) {
        lengthMin = nullSafeMin(this.lengthMin, other.lengthMin);
    }

    private Integer nullSafeMin(Integer a, Integer b){
        if (a == null & b == null) { return null;}
        else if (a == null ) { return b;}
        else if (b == null ) { return a;}
        else return Math.min(a,b);
    }

    @Override
    public Integer getResult() {
        if (lengthMin == null){
            // TODO: decide if -1 is correct null value.. (alternative is Integer.MAX)
            return -1;
        }
        return lengthMin;
    }

    @Override
    protected StringLengthMin create() {
        return new StringLengthMin();
    }
}
