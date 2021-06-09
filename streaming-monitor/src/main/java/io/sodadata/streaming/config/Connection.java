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

/*
This class contains the connection information for the datasource to use.
Currently only connections to a non secure kafka cluster are supported.
*/
public class Connection {
    private ConnectionType type;
    private String host;
    private String port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    //Utility functions
    public String getURL(){
        return String.format("%s:%s", host,port);
    }

    // Only KAFKA is supported for now
    public enum ConnectionType{
        KAFKA
    }

    @Override
    public String toString() {
        return "Connection{" +
                "type=" + type +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
