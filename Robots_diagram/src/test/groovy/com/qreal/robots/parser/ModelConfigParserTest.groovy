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

/**
 * Created by dageev on 26.03.15.
 */
class ModelConfigParserTest extends GroovyTestCase {
    void testParse() {
        def xml = this.getClass().getResource('/model-config.xml').text
        ModelConfigParser modelConfigParser = new ModelConfigParser()
        def devicePorts = modelConfigParser.parse(xml).devicePorts

        assert devicePorts.size() == 25
        assert devicePorts.get("C1") == "angularServomotor"
        assert devicePorts.get("B1") == "encoder95"
    }


}
