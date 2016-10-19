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