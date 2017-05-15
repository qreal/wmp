import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
import {UIDGenerator} from "../controller/UIDGenerator";
import {DiagramElement} from "./DiagramElement";
export class Link implements DiagramElement {

    private logicalId: string;
    private jointObject: joint.dia.Link;
    private constPropertiesPack: PropertiesPack;
    private changeableProperties: Map<string, Property> = new Map<string, Property>();
    private name: string;
    private type: string;

    constructor(jointObject: joint.dia.Link, name: string, type: string, properties: Map<string, Property>) {
        this.logicalId = UIDGenerator.generate();
        this.constPropertiesPack = this.getDefaultConstPropertiesPack();
        this.name = name;
        this.type = type;

        this.jointObject = jointObject;
        var properties: Map<string, Property> = properties;
        this.changeableProperties = properties;
        this.changeLabel(properties.get("Guard").value);
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

    getChangeableProperties(): Map<string, Property> {
        return this.changeableProperties;
    }

    setProperty(key: string, property: Property): void {
        this.changeableProperties.set(key, property);
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
        var logical: Map<string, Property> = this.initConstLogicalProperties();
        var graphical: Map<string, Property> = this.initConstGraphicalProperties();
        return new PropertiesPack(logical, graphical);
    }

    private initConstLogicalProperties(): Map<string, Property> {
        var logical: Map<string, Property> = new Map<string, Property>();
        logical.set("name", new Property("name", "QString", this.name));
        logical.set("linkShape", new Property("linkShape", "int", "-1"));
        logical.set("outgoingExplosion", new Property("outgoingExplosion", "qReal::Id", "qrm:/"));
        return logical;
    }

    private initConstGraphicalProperties(): Map<string, Property> {
        var graphical: Map<string, Property> = new Map<string, Property>();
        graphical.set("name", new Property("name", "QString", this.name));
        graphical.set("configuration", new Property("configuration", "QPolygon", "0, 0 : 0, 0 : "));
        graphical.set("fromPort", new Property("fromPort", "double", "0"));
        graphical.set("toPort", new Property("toPort", "double", "0"));
        graphical.set("position", new Property("position", "QPointF", "0, 0"));
        return graphical;
    }

    private updateHighlight(): void {
        if (!this.jointObject.get('target').id || !this.jointObject.get('source').id) {
            this.jointObject.attr({
                    '.connection': {stroke: 'red'},
                    '.marker-source': {stroke: 'red'},
                    '.marker-target': {stroke: 'red'}
                }
            );
        } else {
            this.jointObject.attr({
                    '.connection': {stroke: 'black'},
                    '.marker-source': {stroke: 'black'},
                    '.marker-target': {stroke: 'black'}
                }
            );
        }
    }
}