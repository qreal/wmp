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

import groovy.transform.TupleConstructor

/**
 * Created by dageev on 29.03.15.
 */
@TupleConstructor
class ModelConfigValidator {

    SystemConfig systemConfig

    public ModelConfigValidator(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    ValidationResult validate(ModelConfig modelConfig) {
        ValidationResult validationResult = new ValidationResult(errors: [])
        modelConfig.devicePorts.each { key, value ->
            systemConfig.devices.each { device ->
                if (device.name == value && device.types.size() > 0) {
                    validationResult.addError("Unable to set $device.name to port $key, when it has deviceTypes")
                }
            }

        }
        return validationResult;

    }
}
