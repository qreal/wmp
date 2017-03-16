import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
import {PropertyEditElement} from "./PropertyEditElement";
import {UIDGenerator} from "../controller/UIDGenerator";
import {DiagramNode} from "./DiagramNode";

class ImageWithPorts extends joint.shapes.basic.Generic {
    constructor(portsModelInterface: joint.shapes.basic.PortsModelInterface) {
        super(portsModelInterface);

        this.set("markup", '<g class="rotatable"><g class="scalable"><rect class ="outer"/><image/></g><text/><g class="inPorts"/><g class="outPorts"/></g>')
        this.set("portMarkup", '<g class="port<%= id %>"><circle/><text/></g>')
    }

    getPortAttrs(portName: string, index: number, total: number, selector: string, type: string): {} {

        var attrs = {};

        var portClass = 'port' + index;
        var portSelector = selector + '>.' + portClass;
        var portTextSelector = portSelector + '>text';
        var portCircleSelector = portSelector + '>circle';

        attrs[portTextSelector] = { text: portName };
        attrs[portCircleSelector] = { port: { id: portName || _.uniqueId(type), type: type } };
        attrs[portSelector] = { ref: 'rect', 'ref-x': (index + 0.5) * (1 / total) };

        if (selector === '.outPorts') {
            attrs[portSelector]['ref-dx'] = 0;
        }

        return attrs;
    }
};

export class DefaultDiagramNode implements DiagramNode {

    private logicalId: string;
    private jointObject: ImageWithPorts;
    private name: string;
    private type: string;
    private constPropertiesPack: PropertiesPack;
    private changeableProperties: Map<String, Property>;
    private imagePath: string;
    private propertyEditElement: PropertyEditElement;

    private resizeParameters = {
        isTopResizing: false,
        isBottomResizing: false,
        isRightResizing: false,
        isLeftResizing: false,
    };

    private lastMousePosition = {
        x: 0,
        y: 0,
    };

    private boundingBox = {
        width: 0,
        height: 0,
    };

    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, id?: string,
                notDefaultConstProperties?: PropertiesPack) {
        this.logicalId = UIDGenerator.generate();
        this.name = name;
        this.type = type;

        this.boundingBox.width = width;
        this.boundingBox.height = height;

        this.constPropertiesPack = DefaultDiagramNode.getDefaultConstPropertiesPack(name);
        if (notDefaultConstProperties) {
            $.extend(this.constPropertiesPack.logical, notDefaultConstProperties.logical);
            $.extend(this.constPropertiesPack.graphical, notDefaultConstProperties.graphical);
        }

        var jointObjectAttributes = {
            position: { x: x, y: y },
            size: { width: this.boundingBox.width, height: this.boundingBox.height },
            outPorts: [''],
            attrs: <{ [selector: string]: { [key: string]: string} }> {
                image: {
                    'xlink:href': imagePath
                },
            }
        };

        if (id) {
            jQuery.extend(jointObjectAttributes, {id: id});
        }

        this.jointObject = new ImageWithPorts(jointObjectAttributes);
        this.changeableProperties = properties;
        this.imagePath = imagePath;
    }

    pointermove(cellView, evt, x, y): void {
        cellView.options.interactive = true;
        var bbox = cellView.getBBox();
        var newX = bbox.x + (<number> (bbox.width - 50)/2);
        var newY = bbox.y + bbox.height - 50;
        this.propertyEditElement.setPosition(newX, newY);

        if (this.resizeParameters.isBottomResizing || this.resizeParameters.isRightResizing)
        {
            cellView.options.interactive = false;
            var model = <joint.dia.Element> cellView.model;
            var diffX = x - this.lastMousePosition.x;
            var diffY = y - this.lastMousePosition.y;
            this.lastMousePosition.x = x;
            this.lastMousePosition.y = y;

            if (this.resizeParameters.isBottomResizing) {
                if (this.resizeParameters.isRightResizing) {
                    this.boundingBox.width = bbox.width + diffX;
                    this.boundingBox.height = bbox.height + diffY;
                } else {
                    this.boundingBox.width = bbox.width;
                    this.boundingBox.height = bbox.height + diffY;
                }
            } else if (this.resizeParameters.isRightResizing) {
                this.boundingBox.width = bbox.width + diffX;
                this.boundingBox.height = bbox.height;
            }
            model.resize(this.boundingBox.width - 2, this.boundingBox.height);
        }
    }

    initPropertyEditElements(zoom: number): void {
        var parentPosition = this.getJointObjectPagePosition(zoom);
        this.propertyEditElement = new PropertyEditElement(this.logicalId, this.jointObject.id,
            this.changeableProperties);
        var propertyEditElementX = parentPosition.x + (<number> (this.boundingBox.width - 50)/2);
        var propertyEditElementY = parentPosition.y + this.boundingBox.height - 50;
        this.propertyEditElement.setPosition(propertyEditElementX, propertyEditElementY);
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

    getSize(): string {
        return String(this.boundingBox.width) + ", " + String(this.boundingBox.height);
    }

    setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void {
        this.jointObject.position(x, y);
        // var position = this.getJointObjectPagePosition(zoom);
        // this.propertyEditElement.setPosition(position.x, position.y);
        var bbox = cellView.getBBox();
        var newX = bbox.x + (<number> (bbox.width - 50)/2);
        var newY = bbox.y + bbox.height - 50;
        this.propertyEditElement.setPosition(newX, newY);
    }

    setSize(width: number, height: number, cellView : joint.dia.CellView): void {
        var model = <joint.dia.Element> cellView.model;
        model.resize(width - 2, height);
        var bbox = cellView.getBBox();
        var newX = bbox.x + (<number> (bbox.width - 50)/2);
        var newY = bbox.y + bbox.height - 50;
        this.propertyEditElement.setPosition(newX, newY);
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

    private static getDefaultConstPropertiesPack(name: string): PropertiesPack {
        var logical: Map<String, Property> = this.initConstLogicalProperties(name);
        var graphical: Map<String, Property> = this.initConstGraphicalProperties(name);
        return new PropertiesPack(logical, graphical);
    }

    private static initConstLogicalProperties(name: string): Map<String, Property> {
        var logical: Map<String, Property> = new Map<String, Property>();
        logical["name"] = new Property("name", "QString", name);
        logical["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        logical["linkShape"] = new Property("linkShape", "int", "0");
        logical["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        logical["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        return logical;
    }

    private static initConstGraphicalProperties(name: string): Map<String, Property> {
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

    initResize(bbox, x: number, y: number, paddingPercent): void {
        this.resizeParameters = {
            isTopResizing: DefaultDiagramNode.isTopBorderClicked(bbox, x, y, paddingPercent),
            isBottomResizing: DefaultDiagramNode.isBottomBorderClicked(bbox, x, y, paddingPercent),
            isRightResizing: DefaultDiagramNode.isRightBorderClicked(bbox, x, y, paddingPercent),
            isLeftResizing: DefaultDiagramNode.isLeftBorderClicked(bbox, x, y, paddingPercent),
        };
        this.lastMousePosition.x = x;
        this.lastMousePosition.y = y;
    }

    completeResize(): void {
        this.resizeParameters = {
            isTopResizing: false,
            isBottomResizing: false,
            isRightResizing: false,
            isLeftResizing: false,
        };
    }

    isResizing() : boolean {
        return this.resizeParameters.isBottomResizing || this.resizeParameters.isRightResizing;
    }

    private static isLeftBorderClicked(bbox, x, y, paddingPercent): boolean {
        return (x <= bbox.x + paddingPercent && x >= bbox.x - paddingPercent &&
        y <= bbox.y + bbox.height + paddingPercent && y >= bbox.y - paddingPercent);
    }

    private static isRightBorderClicked(bbox, x, y, paddingPercent): boolean {
        return (x <= bbox.x + bbox.width + paddingPercent && x >= bbox.x + bbox.width - paddingPercent &&
        y <= bbox.y + bbox.height + paddingPercent && y >= bbox.y - paddingPercent);
    }

    private static isTopBorderClicked(bbox, x, y, paddingPercent): boolean {
        return (x <= bbox.x + bbox.width + paddingPercent && x >= bbox.x - paddingPercent &&
        y <= bbox.y + paddingPercent && y >= bbox.y - paddingPercent);
    }

    private static isBottomBorderClicked(bbox, x, y, paddingPercent): boolean {
        return (x <= bbox.x + bbox.width + paddingPercent && x >= bbox.x - paddingPercent &&
        y <= bbox.y + bbox.height + paddingPercent && y >= bbox.y + bbox.height - paddingPercent);
    }

}