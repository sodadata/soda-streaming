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

package io.sodadata.streaming.metrics;

import org.apache.flink.api.common.functions.AggregateFunction;

public class MessageCount implements AggregateFunction<String, Integer, Integer> {
    @Override
    public Integer createAccumulator() {
        return 0;
    }

    @Override
    public Integer add(String s, Integer integer) {
        return integer + 1;
    }

    @Override
    public Integer getResult(Integer integer) {
        return integer;
    }

    @Override
    public Integer merge(Integer integer, Integer acc1) {
        return integer + acc1;
    }
}
