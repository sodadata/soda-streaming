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

import io.sodadata.streaming.types.Categorical;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullCountTest {

    @Test
    void merge() {
        NullCount metric1 = new NullCount();
        NullCount metric2 = new NullCount();
        for (int i =0; i <7; i++){
            metric1.add(null);
        }
        for (int i =0; i <5; i++){
            metric1.add(new Object());
        }
        for (int i =0; i <3; i++){
            metric2.add(null);
        }
        for (int i =0; i <20; i++){
            metric2.add(new Object());
        }
        metric1.merge(metric2);
        assertEquals(10,metric1.getResult());
    }

    @Test
    void getResult() {
        NullCount metric = new NullCount();
        for (int i =0; i <7; i++){
            metric.add(null);
        }
        for (int i =0; i <5; i++){
            metric.add(new Object());
        }
        assertEquals(7,metric.getResult());
    }

    @Test
    void accepts() {
        NullCount metric = new NullCount();
        assertTrue(metric.accepts(null));
        assertFalse(metric.accepts(new Object()));
    }
}