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

class SubprogramDiagramNode {

    private logicalId: string;
    private properties: Map<Property>;
    private type: string = "SubprogramDiagram";
    private name: string;

    constructor(logicalId: string, name: string) {
        this.logicalId = logicalId;
        this.name = name;
        this.initProperties(name);
    }

    getLogicalId(): string {
        return this.logicalId;
    }

    getType(): string {
        return this.type;
    }

    getName(): string {
        return this.name;
    }

    getProperties(): Map<Property> {
        return this.properties;
    }

    private initProperties(name: string): void {
        this.properties = {};
        this.properties["name"] = new Property("name", "QString", name);
        this.properties["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        this.properties["linkShape"] = new Property("linkShape", "int", "0");
        this.properties["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        this.properties["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
    }
}