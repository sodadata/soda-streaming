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

import io.sodadata.streaming.metrics.aggregation.BaseAggregationMetric;
import org.apache.avro.generic.GenericRecord;

public class MessageCount extends BaseAggregationMetric<GenericRecord, Integer, MessageCount> {

    private Integer accumulator = 0;

    public MessageCount() {
        super("message-count");
    }

    @Override
    public void add(GenericRecord input) {
        accumulator += 1;
    }

    @Override
    public void merge(MessageCount other) {
        accumulator += other.accumulator;
    }

    @Override
    public Integer getResult() {
        return accumulator;
    }

    @Override
    protected MessageCount create() {
        return new MessageCount();
    }
}
