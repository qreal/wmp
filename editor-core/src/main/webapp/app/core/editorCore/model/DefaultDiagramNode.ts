import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
import {PropertyEditElement} from "./PropertyEditElement";
import {UIDGenerator} from "../controller/UIDGenerator";
import {DiagramNode} from "./DiagramNode";
import {DiagramContainer} from "./DiagramContainer";
import {DefaultSize} from "../../../common/constants/DefaultSize";

class ImageWithPorts extends joint.shapes.basic.Generic {
    constructor(portsModelInterface: joint.shapes.basic.PortsModelInterface) {
        super(portsModelInterface);

        this.set("markup", '<g class="rotatable"><g class="scalable"><rect class ="outer"/><image/></g><text/><g class="inPorts"/><g class="outPorts"/></g>');
        this.set("portMarkup", '<g class="port<%= id %>"><circle/><text/></g>')
    }

    getPortAttrs(portName: string, index: number, total: number, selector: string, type: string): {} {

        let attrs = {};

        let portClass = 'port' + index;
        let portSelector = selector + '>.' + portClass;
        let portTextSelector = portSelector + '>text';
        let portCircleSelector = portSelector + '>circle';

        attrs[portTextSelector] = { text: portName };
        attrs[portCircleSelector] = { port: { id: portName || _.uniqueId(type), type: type } };
        attrs[portSelector] = { ref: 'rect', 'ref-x': (index + 0.5) * (1 / total) };

        if (selector === '.outPorts') {
            attrs[portSelector]['ref-dx'] = 0;
        }

        return attrs;
    }
}

/**
 * Class which wraps diagram object.
 */
export class DefaultDiagramNode implements DiagramNode {
    private logicalId: string;

    /**
     * Image object on the screen.
     */
    private jointObject: ImageWithPorts;

    /**
     * Name of diagram.
     */
    private name: string;

    /**
     * Type of diagram.
     */
    private type: string;

    /**
     * Default properties.
     */
    private constPropertiesPack: PropertiesPack;

    /**
     * Properties, which can be changed by user.
     */
    private changeableProperties: Map<String, Property>;

    /**
     * Path to image.
     */
    private imagePath: string;

    /**
     * Text objects which are on the screen
     */
    private propertyEditElements: Map<String, PropertyEditElement>;
    private parentNode: DiagramContainer;

    /**
     * Graph, where diagram and it properties is located.
     */
    private graph : joint.dia.Graph;

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

    /**
     * Coordinates of last diagram position.
     */
    private lastDiagramPosition = {
        x: 0,
        y: 0,
    };

    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<String, Property>, imagePath: string, id?: string, notDefaultConstProperties?: PropertiesPack) {
        this.logicalId = UIDGenerator.generate();
        this.name = name;
        this.type = type;

        this.boundingBox.width = width;
        this.boundingBox.height = height;

        this.constPropertiesPack = DefaultDiagramNode.getDefaultConstPropertiesPack(name);
        if (notDefaultConstProperties !== null && typeof notDefaultConstProperties !== 'undefined') {
            $.extend(this.constPropertiesPack.logical, notDefaultConstProperties.logical);
            $.extend(this.constPropertiesPack.graphical, notDefaultConstProperties.graphical);
        }

        let jointObjectAttributes = {
            position: {x: x, y: y},
            size: {width: this.boundingBox.width, height: this.boundingBox.height},
            outPorts: [''],
            attrs: <{[selector: string]: {[key: string]: string}}> {
                image: {
                    'xlink:href': imagePath
                },
            }
        };

        if (id !== null && typeof id !== 'undefined') {
            jQuery.extend(jointObjectAttributes, {id: id});
        }

        this.propertyEditElements = new Map();
        this.jointObject = new ImageWithPorts(jointObjectAttributes);
        this.changeableProperties = properties;
        this.imagePath = imagePath;
        this.parentNode = null;
    }

    /**
     * Pointermove handler. All parameters comes from default pointermove listener.
     * @param cellView
     * @param evt event.
     * @param x x position of event.
     * @param y y position of event.
     */
    pointermove(cellView, evt, x: number, y: number): void {
        cellView.options.interactive = true;
        let diffX = x - this.lastMousePosition.x;
        let diffY = y - this.lastMousePosition.y;
        this.lastDiagramPosition.x = this.getX();
        this.lastDiagramPosition.y = this.getY();

        if (this.resizeParameters.isBottomResizing || this.resizeParameters.isRightResizing) {
            cellView.options.interactive = false;
            let model = <joint.dia.Element> cellView.model;
            this.lastMousePosition.x = x;
            this.lastMousePosition.y = y;

            if (this.resizeParameters.isBottomResizing) {
                if (this.resizeParameters.isRightResizing) {
                    this.boundingBox.width += diffX;
                    this.boundingBox.height += diffY;
                } else {
                    this.boundingBox.height += diffY;
                }
            } else if (this.resizeParameters.isRightResizing) {
                this.boundingBox.width += diffX;
            }
            model.resize(this.boundingBox.width, this.boundingBox.height);
        }
    }

    initPropertyEditElements(zoom: number, graph: joint.dia.Graph): void {
        this.graph = graph;
        let parentPosition = this.getJointObjectPagePosition(zoom);
        let xIndentFromParent: number = this.boundingBox.width - 50;
        let propertyEditElementX = parentPosition.x + xIndentFromParent;
        let propertyEditElementY = parentPosition.y + this.boundingBox.height;
        let delta = PropertyEditElement.fontSize;

        for (let propertyKey in this.changeableProperties) {
            let property = this.changeableProperties[propertyKey];

            if (property.type === "string") {
                if (property.coordinates !== undefined) {
                    this.restorePropertyPositions(graph);
                    break;
                }

                let diagramCenterX = propertyEditElementX + DefaultSize.DEFAULT_NODE_WIDTH / 2;
                let diagramCenterY = propertyEditElementY;

                let propertyEditElement = new PropertyEditElement(diagramCenterX, diagramCenterY, property, graph, 1);
                propertyEditElementY += delta;

                property.setCoordinates(propertyEditElement.getX(), propertyEditElement.getY());
                this.propertyEditElements[propertyKey] = propertyEditElement;
            }
        }
    }

    restorePropertyPositions(graph: joint.dia.Graph): void {
        for (let propertyKey in this.changeableProperties) {
            let property = this.changeableProperties[propertyKey];
            if (property.type !== "string") {
                continue;
            }

            let xPosition = property.coordinates.x;
            let yPosition = property.coordinates.y;
            let propertyEditElement = new PropertyEditElement(xPosition, yPosition, property, graph, 0);
            this.propertyEditElements[propertyKey] = propertyEditElement;
        }
    }

    getTextProperties() : joint.shapes.basic.Text[] {
        let textObjects = [];

        for (let propertyKey in this.propertyEditElements) {
            textObjects.push(this.propertyEditElements[propertyKey].getTextObject());
        }

        return textObjects;
    }

    getPropertyEditElements(): Map<String, PropertyEditElement> {
        return this.propertyEditElements;
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

    getParentNode(): DiagramContainer {
        return this.parentNode;
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

    /**
     * Changes text position according to new diagram coordinates.
     */
    changeTextPosition() : void {
        let dx = this.getX() - this.lastDiagramPosition.x;
        let dy = this.getY() - this.lastDiagramPosition.y;

        // no need to call it every time.
        if (dx !== 0 || dy !== 0) {
            for (let propertyKey in this.propertyEditElements) {
                let propertyEditElement = this.propertyEditElements[propertyKey];
                propertyEditElement.setRelativePosition(dx, dy);

                let x: number = propertyEditElement.getX();
                let y: number = propertyEditElement.getY();

                this.changeableProperties[propertyKey].setCoordinates(x, y);
            }
        }
    }

    setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void {
        this.jointObject.position(x, y);
    }

    setSize(width: number, height: number, cellView : joint.dia.CellView): void {
        let model = <joint.dia.Element> cellView.model;
        model.resize(width, height);
    }

    setParentNode(parent: DiagramContainer): void {
        if (parent === this.parentNode) {
            return;
        }

        if (this.parentNode !== null && typeof this.parentNode !== 'undefined') {
            this.parentNode.getJointObject().unembed(this.getJointObject());
        }

        this.parentNode = parent;
        if (parent !== null && typeof parent !== 'undefined') {
            parent.getJointObject().embed(this.getJointObject());
        }
    }

    getImagePath(): string {
        return this.imagePath;
    }

    getJointObject(): joint.shapes.basic.Generic {
        return this.jointObject;
    }

    getConstPropertiesPack(): PropertiesPack {
        return this.constPropertiesPack;
    }

    /**
     * Sets new property and shows it on the screen.
     * @param key property key
     * @param property property to set
     */
    setProperty(key: string, property: Property): void {
        this.changeableProperties[key] = property;
        this.propertyEditElements[key].setProperty(property, this.graph);
        let propertyChangedEvent = new CustomEvent('property-changed', {
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
        let logical: Map<String, Property> = this.initConstLogicalProperties(name);
        let graphical: Map<String, Property> = this.initConstGraphicalProperties(name);
        return new PropertiesPack(logical, graphical);
    }

    private static initConstLogicalProperties(name: string): Map<String, Property> {
        let logical: Map<String, Property> = new Map<String, Property>();
        logical["name"] = new Property("name", "QString", name);
        logical["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        logical["linkShape"] = new Property("linkShape", "int", "0");
        logical["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        logical["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        return logical;
    }

    private static initConstGraphicalProperties(name: string): Map<String, Property> {
        let graphical: Map<String, Property> = new Map<String, Property>();
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