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

package io.sodadata.streaming.config;

import java.util.List;

public class Scan {
    private String stream_name;
    private RegistryType schema;
    private SchemaType schema_type;
    private List<String> metrics;

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public RegistryType getSchema() {
        return schema;
    }

    public void setSchema(RegistryType schema) {
        this.schema = schema;
    }

    public SchemaType getSchema_type() {
        return schema_type;
    }

    public void setSchema_type(SchemaType schema_type) {
        this.schema_type = schema_type;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }


    // Only INTERNAL_REGISTRY is supported for now
    public enum RegistryType{
        INTERNAL_REGISTRY,
        ;
//        EXTERNAL_REGISTRY,
//        AUTO
    }

    // Only AVRO is supported for now
    public enum SchemaType{
        AVRO,
//        JSON,
//        PROTOBUF
    }
}
