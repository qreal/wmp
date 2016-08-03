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

/// <reference path="DiagramElement.ts" />
/// <reference path="PropertiesPack.ts" />
/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../vendor.d.ts" />

class Link implements DiagramElement {

    private logicalId: string;
    private jointObject: joint.dia.Link;
    private constPropertiesPack: PropertiesPack;
    private changeableProperties: Map<Property> = {};
    private name = "Link"
    private type = "ControlFlow";

    constructor(jointObject: joint.dia.Link, properties: Map<Property>) {
        this.logicalId = UIDGenerator.generate();
        this.constPropertiesPack = this.getDefaultConstPropertiesPack();

        this.jointObject = jointObject;
        this.changeableProperties = properties;
        this.changeLabel(properties["Guard"].value);
        this.updateHighlight();

        jointObject.on('change:source', () => {
                this.updateHighlight();
            }
        );

        jointObject.on('change:target', () => {
                this.updateHighlight();
            }
        );
    }

    getLogicalId(): string {
        return this.logicalId;
    }

    getJointObject() {
        return this.jointObject;
    }

    getName(): string {
        return this.name;
    }

    getType(): string {
        return this.type;
    }

    getConstPropertiesPack(): PropertiesPack {
        return this.constPropertiesPack;
    }

    getChangeableProperties(): Map<Property> {
        return this.changeableProperties;
    }

    setProperty(key: string, property: Property): void {
        this.changeableProperties[key] = property;
        if (key === "Guard") {
            this.changeLabel(property.value);
        }
        var propertyChangedEvent = new CustomEvent('property-changed', {
            detail: {
                nodeId: this.getLogicalId(),
                key: key,
                value: property.value
            }
        });
        document.dispatchEvent(propertyChangedEvent);
    }

    private changeLabel(value: string): void {
        this.jointObject.label(0, {
            position: 0.5,
            attrs: {
                text: {
                    text: value , 'font-size': 14
                }
            }
        });
    }

    private getDefaultConstPropertiesPack(): PropertiesPack {
        var logical: Map<Property> = this.initConstLogicalProperties();
        var graphical: Map<Property> = this.initConstGraphicalProperties();
        return new PropertiesPack(logical, graphical);
    }

    private initConstLogicalProperties(): Map<Property> {
        var logical: Map<Property> = {};
        logical["name"] = new Property("name", "QString", this.name);
        logical["linkShape"] = new Property("linkShape", "int", "-1");
        logical["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        return logical;
    }

    private initConstGraphicalProperties(): Map<Property> {
        var graphical: Map<Property> = {};
        graphical["name"] = new Property("name", "QString", this.name);
        graphical["configuration"] = new Property("configuration", "QPolygon", "0, 0 : 0, 0 : ");
        graphical["fromPort"] = new Property("fromPort", "double", "0");
        graphical["toPort"] = new Property("toPort", "double", "0");
        graphical["position"] = new Property("position", "QPointF", "0, 0");
        return graphical;
    }

    private updateHighlight(): void {
        if (!this.jointObject.get('target').id || !this.jointObject.get('source').id) {
            this.jointObject.attr({
                    '.connection': {stroke: 'red'},
                    '.marker-target': {fill: 'red', d: 'M 10 0 L 0 5 L 10 10 z'}
                }
            );
        } else {
            this.jointObject.attr({
                    '.connection': {stroke: 'black'},
                    '.marker-target': {fill: 'black', d: 'M 10 0 L 0 5 L 10 10 z'}
                }
            );
        }
    }
}