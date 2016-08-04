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

/// <reference path="../../interfaces/robotModel/PortInfo.ts" />
/// <reference path="../../types/Direction.ts" />
/// <reference path="../../types/ReservedVariableType.ts" />

class PortInfoImpl implements PortInfo {
    private name: string;
    private direction: Direction;
    private nameAliases: string[] = [];
    private reservedVariable: string;
    private reservedVariableType: ReservedVariableType = ReservedVariableType.scalar;

    constructor(name: string, direction: Direction, nameAliases?: string[],
                reservedVariable?: string, reservedVariableType?: ReservedVariableType) {
        this.name = name;
        this.direction = direction;
        this.nameAliases = nameAliases;
        this.reservedVariable = reservedVariable;
        this.reservedVariableType = reservedVariableType;
    }

    getName(): string {
        return this.name;
    }

    getDirection(): Direction {
        return this.direction;
    }

    getNameAliases(): string[] {
        return this.nameAliases;
    }

    getReservedVariable(): string {
        return this.reservedVariable;
    }

    getReservedVariableType(): ReservedVariableType {
        return this.reservedVariableType;
    }
}