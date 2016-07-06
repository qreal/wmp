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
class SystemConfigParserTest extends GroovyTestCase {

    void testParse() {
        def xml = this.getClass().getResource('/system-config.xml').text
        SystemConfigParser systemConfigParser = new SystemConfigParser()

        def systemConfig = systemConfigParser.parse(xml);
        def devices = systemConfig.devices
        def ports = systemConfig.ports

        assert devices.size() == 16
        assert ports.size() == 25

        def servoMotor = devices.get(0)
        assert servoMotor.name == "servoMotor"
        assert servoMotor.availablePorts == ["E1", "E2", "E3", "C1", "C2", "C3"]
        def angularAttributes = [min: "600000", max: "2200000", zero: "1400000", stop: "0", type: "angular"]
        def continuousAttributes = [min : "600000", max: "2200000", zero: "1400000", stop: "0",
                                    type: "continuousRotation"]
        assert servoMotor.types == [new DeviceType("angularServomotor", angularAttributes),
                                    new DeviceType("continuousRotationServomotor", continuousAttributes)]
    }
}
