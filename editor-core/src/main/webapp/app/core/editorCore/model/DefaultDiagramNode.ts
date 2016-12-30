/// <reference path="DiagramNode.ts" />
/// <reference path="PropertiesPack.ts" />
/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="PropertyEditElement.ts" />
/// <reference path="../../../vendor.d.ts" />

class DefaultDiagramNode implements DiagramNode {

    private logicalId: string;
    private jointObject: joint.shapes.devs.ImageWithPorts;
    private name: string;
    private type: string;
    private constPropertiesPack: PropertiesPack;
    private changeableProperties: Map<Property>;
    private imagePath: string;
    private propertyEditElement: PropertyEditElement;

    private isTopResizing: boolean = false;
    private isBottomResizing: boolean = false;
    private isRightResizing: boolean = false;
    private isLeftResizing: boolean = false;
    private lastMousePositionX;
    private lastMousePositionY;
    private bboxWidth : number;
    private bboxHeight : number;


    constructor(name: string, type: string, x: number, y: number, width: number, height: number,
                properties: Map<Property>, imagePath: string,
                id?: string, notDefaultConstProperties?: PropertiesPack) {
        this.logicalId = UIDGenerator.generate();
        this.name = name;
        this.type = type;

        this.isTopResizing = false;
        this.isBottomResizing = false;
        this.isRightResizing = false;
        this.isLeftResizing = false;

        this.bboxWidth = width;
        this.bboxHeight = height;

        this.constPropertiesPack = this.getDefaultConstPropertiesPack(name);
        if (notDefaultConstProperties) {
            $.extend(this.constPropertiesPack.logical, notDefaultConstProperties.logical);
            $.extend(this.constPropertiesPack.graphical, notDefaultConstProperties.graphical);
        }

        var jointObjectAttributes = {
            position: { x: x, y: y },
            size: { width: this.bboxWidth, height: this.bboxHeight },
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
    }

    pointermove(cellView, evt, x, y) : void {

        cellView.options.interactive = true;
        var bbox = cellView.getBBox();
        var newX = bbox.x + (<number> (bbox.width - 50)/2);
        var newY = bbox.y + bbox.height - 50;
        this.propertyEditElement.setPosition(newX, newY);

        if (this.isBottomResizing || this.isRightResizing)
        {
            cellView.options.interactive = false;
            var model = <joint.dia.Element> cellView.model;
            var diffX = x - this.lastMousePositionX;
            var diffY = y - this.lastMousePositionY;
            this.lastMousePositionX = x;
            this.lastMousePositionY = y;

            if (this.isBottomResizing) {
                if (this.isRightResizing) {
                    this.bboxWidth = bbox.width + diffX;
                    this.bboxHeight = bbox.height + diffY;
                    model.resize(bbox.width - 2 + diffX, bbox.height + diffY);
                } else {
                    this.bboxWidth = bbox.width;
                    this.bboxHeight = bbox.height + diffY;
                    model.resize(bbox.width - 2, bbox.height + diffY);
                }
            } else if (this.isRightResizing) {
                this.bboxWidth = bbox.width + diffX;
                this.bboxHeight = bbox.height;
                model.resize(bbox.width - 2 + diffX, bbox.height);
                return;
            }
        }
    };

    initPropertyEditElements(zoom: number): void {
        var parentPosition = this.getJointObjectPagePosition(zoom);
        this.propertyEditElement = new PropertyEditElement(this.logicalId, this.jointObject.id,
            this.changeableProperties);
        var propertyEditElementX = parentPosition.x + (<number> (this.bboxWidth - 50)/2);
        var propertyEditElementY = parentPosition.y + this.bboxHeight - 50;
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

    getSize() : string {
        return String(this.bboxWidth) + ", " + String(this.bboxHeight);
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

    getChangeableProperties(): Map<Property> {
        return this.changeableProperties;
    }

    private getDefaultConstPropertiesPack(name: string): PropertiesPack {
        var logical: Map<Property> = this.initConstLogicalProperties(name);
        var graphical: Map<Property> = this.initConstGraphicalProperties(name);
        return new PropertiesPack(logical, graphical);
    }

    private initConstLogicalProperties(name: string): Map<Property> {
        var logical: Map<Property> = {};
        logical["name"] = new Property("name", "QString", name);
        logical["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        logical["linkShape"] = new Property("linkShape", "int", "0");
        logical["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        logical["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        return logical;
    }

    private initConstGraphicalProperties(name: string): Map<Property> {
        var graphical: Map<Property> = {};
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

    setResizingFields(bbox, x: number, y: number, paddingPercent) : void {
        this.isTopResizing = DefaultDiagramNode.isTopBorderClicked(bbox, x, y, paddingPercent);
        this.isBottomResizing = DefaultDiagramNode.isBottomBorderClicked(bbox, x, y, paddingPercent);
        this.isRightResizing = DefaultDiagramNode.isRightBorderClicked(bbox, x, y, paddingPercent);
        this.isLeftResizing = DefaultDiagramNode.isLeftBorderClicked(bbox, x, y, paddingPercent);
        this.lastMousePositionX = x;
        this.lastMousePositionY = y;
    }
    clearResizingFlags() : void {
        this.isTopResizing = false;
        this.isBottomResizing = false;
        this.isRightResizing = false;
        this.isLeftResizing = false;
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
