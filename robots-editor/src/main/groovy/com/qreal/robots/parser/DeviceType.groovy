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

import groovy.transform.EqualsAndHashCode

/**
 * Created by dageev on 03.04.15.
 */
@EqualsAndHashCode(includeFields = true)
class DeviceType {
    String name
    Map<String, String> properties

    public DeviceType(String name, Map<String, String> properties) {
        this.name = name
        this.properties = properties
    }

    void addProperty(String key, String value) {
        properties.put(key, value)
    }

    String getProperty(String key) {
        return properties.get(key)
    }

    String addProperties(Map properties) {
        this.properties.putAll(properties)
    }
}
