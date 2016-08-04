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

/// <reference path="Map.ts" />
/// <reference path="Property.ts" />

class RobotsDiagramNode {

    private logicalId: string;
    private graphicalId: string;
    private properties: Map<Property>;
    private name: string = "Robot`s Behaviour Diagram";
    private type: string = "RobotsDiagramNode";

    constructor(logicalId: string, graphicalId: string, properties: Map<Property>) {
        this.logicalId = logicalId;
        this.graphicalId = graphicalId;
        this.properties = properties;
    }

    getLogicalId(): string {
        return this.logicalId;
    }

    getGraphicalId(): string {
        return this.graphicalId;
    }

    getProperties(): Map<Property> {
        return this.properties;
    }

    getName(): string {
        return this.name;
    }

    getType(): string {
        return this.type;
    }
}