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

import io.sodadata.streaming.formats.avro.TestGenericRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MessageCountTest {

    @Test
    void merge() {
        MessageCount metric1 = new MessageCount();
        for(int i = 0; i < 10; ++i){
            metric1.add(new TestGenericRecord());
        }
        MessageCount metric2 = new MessageCount();
        for(int i = 0; i < 5; ++i){
            metric2.add(new TestGenericRecord());
        }
        metric1.merge(metric2);
        assertEquals(15,metric1.getResult());
    }

    @Test
    void getResult() {
        MessageCount metric = new MessageCount();
        for(int i = 0; i < 10; ++i){
            metric.add(new TestGenericRecord());
        }
        assertEquals(10,metric.getResult());
    }
}