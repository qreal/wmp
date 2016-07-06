/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.parser

import groovy.xml.MarkupBuilder

/**
 * Created by dageev on 26.03.15.
 */

class ModelConfig {
    Map<String, String> devicePorts
    Map<String, Map<String, String>> typeProperties;

    def ModelConfig(Map<String, String> devicePorts) {
        this.devicePorts = devicePorts
    }

    def ModelConfig(Map<String, String> devicePorts, List<Map<String, String>> typeProperties) {
        this.devicePorts = devicePorts
        this.typeProperties = getTypePropertiesMap(typeProperties)
    }

    Map<String, Map<String, String>> getTypePropertiesMap(List<Map<String, String>> mapList) {
        Map<String, Map<String, String>> typeProperties = new HashMap<>();
        mapList.each { map ->
            String type = map.type
            Map<String, String> newMap = [:]
            newMap.putAll(map)
            newMap.remove("type")
            typeProperties.put(type, newMap)
        }
        return typeProperties
    }

    def getDeviceName(String portName) {
        return devicePorts.get(portName)
    }

    String convertToXml() {
        def sw = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(sw)
        builder.config {
            "initScript"()
            devicePorts.each { key, value ->
                "$key" {
                    "$value"(typeProperties.get(value))
                }
            }
        }
        return sw.toString()
    }
}
