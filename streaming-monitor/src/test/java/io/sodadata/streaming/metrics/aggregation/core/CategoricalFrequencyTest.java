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

class CategoricalFrequencyTest {

    @Test
    void merge() {
        CategoricalFrequency metric1 = new CategoricalFrequency();
        CategoricalFrequency metric2 = new CategoricalFrequency();
        for (int i =0; i <7; i++){
            metric1.add(new Categorical("1"));
        }
        for (int i =0; i <53; i++){
            metric2.add(new Categorical("2"));
        }
        for (int i =0; i <40; i++){
            metric2.add(new Categorical("3"));
        }
        metric1.merge(metric2);
        var result = metric1.getResult();
        assertEquals(0.07,result.get("1"));
        assertEquals(0.53,result.get("2"));
        assertEquals(0.40,result.get("3"));
    }

    @Test
    void getResult() {
        CategoricalFrequency metric = new CategoricalFrequency();
        for (int i =0; i <7; i++){
            metric.add(new Categorical("1"));
        }
        for (int i =0; i <53; i++){
            metric.add(new Categorical("2"));
        }
        for (int i =0; i <40; i++){
            metric.add(new Categorical("3"));
        }
        var result = metric.getResult();
        assertEquals(0.07,result.get("1"));
        assertEquals(0.53,result.get("2"));
        assertEquals(0.40,result.get("3"));
    }
}