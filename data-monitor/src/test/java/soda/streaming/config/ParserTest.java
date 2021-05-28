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

package soda.streaming.config;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse_existing_warehouse_file() {
        assertDoesNotThrow(() -> Parser.parseWarehouseFile("warehouse_test.yml"));
    }

    @Test
    void parse_non_existing_warehouse_file_should_throw() {
        assertThrows(FileNotFoundException.class, () -> Parser.parseWarehouseFile("non-existing.yml"));
    }

    @Test
    void parse_valid_warehouse_file() {
        assertDoesNotThrow(() -> Parser.parseWarehouseFile("warehouse_test.yml"));
        Warehouse warehouse = null;
        try {
            warehouse = Parser.parseWarehouseFile("warehouse_test.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(warehouse);
        assertNotNull(warehouse.getName());
        assertNotNull(warehouse.getConnection());
        assertNotNull(warehouse.getConnection().getHost());
        assertNotNull(warehouse.getConnection().getPort());
        assertNotNull(warehouse.getConnection().getType());

        assertEquals(warehouse.getName(),"test_warehouse");
        assertEquals(warehouse.getConnection().getHost(),"localhost");
        assertEquals(warehouse.getConnection().getPort(),"9992");
        assertEquals(warehouse.getConnection().getType(), Connection.ConnectionType.KAFKA);

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

        assertEquals(scan.getStream_name(),"test");
        assertEquals(scan.getSchema_type(), Scan.SchemaType.AVRO);
        assertEquals(scan.getSchema(), Scan.RegistryType.INTERNAL_REGISTRY);
        assertEquals(scan.getMetrics(), Arrays.asList("some_metric","some_other_metric"));
    }
}