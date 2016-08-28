/*
 * Copyright Vladimir Zakharov 
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

/// <reference path="DeviceImpl" />

class Motor extends DeviceImpl {
    static parentType  = DeviceImpl;
    static name: string = "motor";
    static friendlyName: string = "Motor";

    private power: number;

    constructor(power: number = 0) {
        super();
        this.power = power;
    }

    public getPower(): number {
        return this.power;
    }

    public setPower(power: number): void {
        this.power = power;
    }

}