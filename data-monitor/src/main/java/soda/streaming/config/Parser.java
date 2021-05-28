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

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class Parser {
    public static Warehouse parseWarehouseFile(String path) throws FileNotFoundException {
        LoaderOptions loaderOpts = new LoaderOptions();
        loaderOpts.setEnumCaseSensitive(false);
        Constructor c = new Constructor(Warehouse.class, loaderOpts);
        Yaml yaml = new Yaml(c);
        InputStream is = Parser.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(String.format("Received null retrieving the file from resources: %s", path));
        }
        return yaml.load(is);
    }

    public static Scan parseScanFile(String path) throws FileNotFoundException {
        LoaderOptions loaderOpts = new LoaderOptions();
        loaderOpts.setEnumCaseSensitive(false);
        Constructor c = new Constructor(Scan.class, loaderOpts);
        c.setEnumCaseSensitive(false);
        Yaml yaml = new Yaml(c);
        InputStream is = Parser.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(String.format("Received null retrieving the file from resources: %s", path));
        }
        return yaml.load(is);
    }
}
