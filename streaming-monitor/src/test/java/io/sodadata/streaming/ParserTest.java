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

package io.sodadata.streaming;

import io.sodadata.streaming.config.Connection;
import io.sodadata.streaming.config.Datasource;
import io.sodadata.streaming.config.Parser;
import io.sodadata.streaming.config.Scan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse_existing_warehouse_file() {
        Assertions.assertDoesNotThrow(() -> Parser.parseDatasourceFile("warehouse_test.yml"));
    }

    @Test
    void parse_non_existing_warehouse_file_should_throw() {
        assertThrows(FileNotFoundException.class, () -> Parser.parseDatasourceFile("non-existing.yml"));
    }

    @Test
    void parse_valid_warehouse_file() {
        assertDoesNotThrow(() -> Parser.parseDatasourceFile("warehouse_test.yml"));
        Datasource datasource = null;
        try {
            datasource = Parser.parseDatasourceFile("warehouse_test.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(datasource);
        assertNotNull(datasource.getName());
        assertNotNull(datasource.getConnection());
        assertNotNull(datasource.getConnection().getHost());
        assertNotNull(datasource.getConnection().getPort());
        assertNotNull(datasource.getConnection().getType());

        assertEquals(datasource.getName(),"test_warehouse");
        assertEquals(datasource.getConnection().getHost(),"localhost");
        assertEquals(datasource.getConnection().getPort(),"9992");
        Assertions.assertEquals(datasource.getConnection().getType(), Connection.ConnectionType.KAFKA);

    }

    @Test
    void parse_existing_scan_file() {
        assertDoesNotThrow(() -> Parser.parseScanFile("scan_test.yml"));
    }

    @Test
    void parse_non_existing_scan_file_should_throw() {
        assertThrows(FileNotFoundException.class, () -> Parser.parseScanFile("non-existing.yml"));
    }

    @Test
    void parse_valid_scan_file() {
        assertDoesNotThrow(() -> Parser.parseScanFile("scan_test.yml"));
        Scan scan = null;
        try {
            scan = Parser.parseScanFile("scan_test.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(scan);
        assertNotNull(scan.getStream_name());
        assertNotNull(scan.getSchema_type());
        assertNotNull(scan.getSchema());
        assertNotNull(scan.getMetrics());
        assertNull(scan.getColumns());

        assertEquals(scan.getStream_name(),"test");
        assertEquals(scan.getSchema_type(), Scan.SchemaType.AVRO);
        assertEquals(scan.getSchema(), Scan.RegistryType.INTERNAL_REGISTRY);
        assertEquals(scan.getMetrics(), Arrays.asList("some_metric","some_other_metric"));
    }

    @Test
    void parse_valid_scan_with_column_metrics_file() {
        assertDoesNotThrow(() -> Parser.parseScanFile("scan_columns_test.yml"));
        Scan scan = null;
        try {
            scan = Parser.parseScanFile("scan_columns_test.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(scan);
        assertNotNull(scan.getStream_name());
        assertNotNull(scan.getSchema_type());
        assertNotNull(scan.getSchema());
        assertNotNull(scan.getMetrics());
        assertNotNull(scan.getColumns());

        assertEquals(scan.getStream_name(),"test");
        assertEquals(scan.getSchema_type(), Scan.SchemaType.AVRO);
        assertEquals(scan.getSchema(), Scan.RegistryType.INTERNAL_REGISTRY);
        assertEquals(scan.getMetrics(), Arrays.asList("some_metric","some_other_metric"));
        assertEquals(scan.getColumns().get("some_col"), Arrays.asList("metric_a","metric_b"));
        assertEquals(scan.getColumns().get("some_other_col"), Arrays.asList("metric_b","metric_c"));
    }
}