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

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;

/*
This class contains all the logic to parse the scan yaml config files and the datasource yaml config file.
It parses them to their respective classes.
*/
public class Parser {
    public static Datasource parseDatasourceFile(String path) throws FileNotFoundException {
        LoaderOptions loaderOpts = new LoaderOptions();
        loaderOpts.setEnumCaseSensitive(false);
        Constructor c = new Constructor(Datasource.class, loaderOpts);
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
            System.out.printf("FileNotFoundException: Received null retrieving the file from resources: %s%n", path);
            throw new FileNotFoundException(String.format("Received null retrieving the file from resources: %s", path));
        }
        return yaml.load(is);
    }

    public static List<Scan> parseScanDirectory(String path) throws IOException {
        List<String> scanFiles = new ArrayList<>();
        try {
            scanFiles = getScanFilesInDirectory(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        List<Scan> scans = new ArrayList<>();
        for(String scanFile: scanFiles){
            try {
                scans.add(parseScanFile(scanFile));
            } catch (FileNotFoundException e){
                throw new FileNotFoundException(String.format("Could not parse scan file:%s",scanFile));
            }
        }
        if (scans.isEmpty()) {
            throw new FileNotFoundException("No parsable scan files found");
        }
        return scans;
    }

    static private List<String> getScanFilesInDirectory(String dir) throws URISyntaxException, IOException {
        URI uri = Objects.requireNonNull(Parser.class.getClassLoader().getResource(dir)).toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath(dir);
        } else {
            myPath = Paths.get(uri);
        }
        List<String> scanFiles = new ArrayList<>();
        for (Iterator<Path> it = Files.walk(myPath, 1).iterator(); it.hasNext(); ) {
            Path path = it.next();
            if (path.toString().endsWith(".yml")){
                scanFiles.add(String.format("%s/%s",dir,path.getFileName().toString()));
            }
        }
        return scanFiles;
    }
}
