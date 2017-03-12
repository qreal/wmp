import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
import {PropertyEditElement} from "./PropertyEditElement";
import {UIDGenerator} from "../controller/UIDGenerator";
import {DiagramNode} from "./DiagramNode";
import {ChangeElementEvent} from "../events/ChangeElementEvent";
export class DefaultDiagramNode extends DiagramNode {

    private logicalId: string;
    private jointObject: joint.shapes.devs.ImageWithPorts;
    private name: string;
    private type: string;
    private constPropertiesPack: PropertiesPack;
    private changeableProperties: Map<String, Property>;
    private imagePath: string;
    private propertyEditElement: PropertyEditElement;

    constructor(logicalId : string, name: string, type: string, x: number, y: number, properties: Map<String, Property>, imagePath: string,
                id?: string, notDefaultConstProperties?: PropertiesPack) {
        super();
        this.logicalId = logicalId;
        this.name = name;
        this.type = type;
        this.constPropertiesPack = this.getDefaultConstPropertiesPack(name);
        if (notDefaultConstProperties) {
            $.extend(this.constPropertiesPack.logical, notDefaultConstProperties.logical);
            $.extend(this.constPropertiesPack.graphical, notDefaultConstProperties.graphical);
        }

        var jointObjectAttributes = {
            position: { x: x, y: y },
            size: { width: 50, height: 50 },
            outPorts: [''],
            attrs: {
                image: {
                    'xlink:href': imagePath
                },
            }
        };

        if (id) {
            jQuery.extend(jointObjectAttributes, {id: id});
        }

        this.jointObject = new joint.shapes.devs.ImageWithPorts(jointObjectAttributes);
        this.changeableProperties = properties;
        this.imagePath = imagePath;

        this.subscribeJointEventsToSystemEvents();

    }

    private subscribeJointEventsToSystemEvents() : void {
        this.jointObject.on('transition:end', function() { ChangeElementEvent.signalEvent() });
        this.jointObject.on('change:size', function() { ChangeElementEvent.signalEvent() });
        this.jointObject.on('change:attrs', function() { ChangeElementEvent.signalEvent() });
    }

    initPropertyEditElements(zoom: number): void {
        var parentPosition = this.getJointObjectPagePosition(zoom);
        this.propertyEditElement = new PropertyEditElement(this.logicalId, this.jointObject.id,
            this.changeableProperties);
        this.propertyEditElement.setPosition(parentPosition.x, parentPosition.y);
        this.jointObject.on('change:position', () => {
            var position = this.getJointObjectPagePosition(zoom);
            this.propertyEditElement.setPosition(position.x, position.y);
        });
    }
    
    getPropertyEditElement(): PropertyEditElement {
        return this.propertyEditElement;
    }

    getLogicalId(): string {
        return this.logicalId;
    }

    getName(): string {
        return this.name;
    }

    getType(): string {
        return this.type;
    }

    getX(): number {
        return (this.jointObject.get("position"))['x'];
    }

    getY(): number {
        return (this.jointObject.get("position"))['y'];
    }

    setPosition(x: number, y: number, zoom: number): void {
        this.jointObject.position(x, y);
        var position = this.getJointObjectPagePosition(zoom);
        this.propertyEditElement.setPosition(position.x, position.y);
    }

    getImagePath(): string {
        return this.imagePath;
    }

    getJointObject() {
        return this.jointObject;
    }

    getConstPropertiesPack(): PropertiesPack {
        return this.constPropertiesPack;
    }

    setProperty(key: string, property: Property): void {
        this.changeableProperties[key] = property;
        var propertyChangedEvent = new CustomEvent('property-changed', {
            detail: {
                nodeId: this.getLogicalId(),
                key: key,
                value: property.value
            }
        });
        document.dispatchEvent(propertyChangedEvent);
    }

    getChangeableProperties(): Map<String, Property> {
        return this.changeableProperties;
    }

    private getDefaultConstPropertiesPack(name: string): PropertiesPack {
        var logical: Map<String, Property> = this.initConstLogicalProperties(name);
        var graphical: Map<String, Property> = this.initConstGraphicalProperties(name);
        return new PropertiesPack(logical, graphical);
    }

    private initConstLogicalProperties(name: string): Map<String, Property> {
        var logical: Map<String, Property> = new Map<String, Property>();
        logical["name"] = new Property("name", "QString", name);
        logical["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        logical["linkShape"] = new Property("linkShape", "int", "0");
        logical["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        logical["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        return logical;
    }

    private initConstGraphicalProperties(name: string): Map<String, Property> {
        var graphical: Map<String, Property> = new Map<String, Property>();
        graphical["name"] = new Property("name", "QString", name);
        graphical["to"] = new Property("to", "qreal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        graphical["configuration"] = new Property("configuration", "QPolygon", "0, 0 : 50, 0 : 50, 50 : 0, 50 : ");
        graphical["fromPort"] = new Property("fromPort", "double", "0");
        graphical["toPort"] = new Property("toPort", "double", "0");
        graphical["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        return graphical;
    }

    private getJointObjectPagePosition(zoom: number): {x: number, y: number} {
        return {
            x: this.jointObject.get("position")['x'] * zoom,
            y: this.jointObject.get("position")['y'] * zoom
        };
    }

}