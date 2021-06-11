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

class NumberMinTest {

    @Test
    void merge() {
        NumberMin metric1 = new NumberMin();
        NumberMin metric2 = new NumberMin();
        for(int i = 12; i < 20; ++i){
            metric1.add(i);
        }
        for(int i = 6; i < 35; ++i){
            metric2.add(i);
        }
        metric1.merge(metric2);
        assertEquals(6,metric1.getResult());
    }

    @Test
    void getResult() {
        NumberMin metric = new NumberMin();
        for(int i = 12; i < 20; ++i){
            metric.add(i);
        }
        assertEquals(12,metric.getResult());
    }
}