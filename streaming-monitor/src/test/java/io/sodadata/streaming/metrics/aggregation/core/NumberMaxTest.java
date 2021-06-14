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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberMaxTest {

    @Test
    void merge() {
        NumberMax metric1 = new NumberMax();
        NumberMax metric2 = new NumberMax();
        for(int i = 0; i < 10; ++i){
            metric1.add(i);
        }
        for(int i = 10; i < 20; ++i){
            metric2.add(i);
        }
        metric1.merge(metric2);
        assertEquals(19,metric1.getResult());
    }

    @Test
    void getResult() {
        NumberMax metric = new NumberMax();
        for(int i = 0; i < 10; ++i){
            metric.add(i);
        }
        assertEquals(9,metric.getResult());
    }
}