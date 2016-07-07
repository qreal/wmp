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

declare class UIDGenerator {
    static generate(): string;
}

declare class PropertiesPack {

    logical: Map<Property>;
    graphical: Map<Property>;

    constructor(logical: Map<Property>, graphical: Map<Property>);

}

declare class Link implements DiagramElement {

    constructor(jointObject: joint.dia.Link, properties: Map<Property>);
    getLogicalId(): string;
    getJointObject(): any;
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<Property>;
    setProperty(key: string, property: Property): void;

}

declare interface DiagramElement {

    getLogicalId(): string;
    getJointObject();
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<Property>;
    setProperty(name: string, property: Property): void;

}

declare interface DiagramNode extends DiagramElement {

    getX(): number;
    getY(): number;
    getImagePath(): string;
    setPosition(x: number, y: number, zoom: number): void;
    getChangeableProperties(): Map<Property>;
    initPropertyEditElements(zoom: number): void;

}

declare class SubprogramDiagramNode {

    constructor(logicalId: string, name: string);
    getLogicalId(): string;
    getName(): string;
    getType(): string;
    getProperties(): Map<Property>;

}

declare class RobotsDiagramNode {

    constructor(logicalId: string, graphicalId: string, properties: Map<Property>);
    getLogicalId(): string;
    getGraphicalId(): string;
    getName(): string;
    getType(): string;
    getProperties(): Map<Property>;

}

declare class PropertyEditElement {

    constructor(logicalId: string, jointObjectId: string, propertyKey: string, property: Property);
    public getHtmlElement();
    public setPosition(x: number, y: number): void;

}

declare class DefaultDiagramNode implements DiagramNode {

    constructor(name: string, type: string, x: number, y: number, properties: Map<Property>, imagePath: string,
                id?: string, notDefaultConstProperties?: PropertiesPack);
    getLogicalId(): string;
    getJointObject(): any;
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<Property>;
    setProperty(key: string, property: Property): void;
    getImagePath(): string;
    getX(): number;
    getY(): number;
    setPosition(x: number, y: number): void;
    getChangeableProperties(): Map<Property>;
    initPropertyEditElements(zoom: number): void;
    getPropertyEditElement(): PropertyEditElement
}

declare interface Map<T> {
    [key: string]: T;
}

declare class NodeType {

    constructor(name: string, propertiesMap: Map<Property>, image?: string);
    getName(): string;
    getPropertiesMap(): Map<Property>;
    getImage(): string;

}

declare class Property {

    name: string;
    type: string;
    value: string;

    constructor(name: string, type: string, value: string);
}

declare class SubprogramNode extends DefaultDiagramNode {

    constructor(name: string, type: string, x: number, y: number, properties: Map<Property>, imagePath: string,
                subprogramDiagramId: string, id?: string, notDefaultConstProperties?: PropertiesPack);
    getSubprogramDiagramId(): string;
    getTextObject(): joint.shapes.basic.Text;

}

declare class DiagramPaper {

    constructor(graph: joint.dia.Graph);
    public getGridSize(): number;
    public getZoom(): number;
    public getNodesMap(): Map<DiagramNode>;
    public getLinksMap(): Map<Link>;
    public getNodeById(id: string): DiagramNode;
    public getLinkById(id: string): Link;
    public addNodesFromMap(nodesMap: Map<DiagramNode>): void;
    public addLinksFromMap(linksMap: Map<Link>): void;
    public addLinkToMap(linkId: string, linkObject: Link): void;
    public addLinkToPaper(link: Link): void;
    public removeNode(nodeId: string): void;
    public removeLink(linkId: string): void;
    public clear(): void;
    public createDefaultNode(name: string, type: string, x: number, y: number, properties: Map<Property>,
                             imagePath: string, id?: string): DiagramNode;
    public createSubprogramNode(name: string, type: string, x: number, y: number, properties: Map<Property>,
                                imagePath: string, subprogramDiagramId: string, id?: string): SubprogramNode;

}

declare class PaletteTypes {
    categories: Map<Map<NodeType>>;
}

declare class ElementTypes {
    uncategorisedTypes: Map<NodeType>;
    paletteTypes: PaletteTypes;
}

declare class DiagramParts {

    nodesMap: Map<DiagramNode>;
    linksMap: Map<Link>;
    robotsDiagramNode: RobotsDiagramNode;
    subprogramDiagramNodes: SubprogramDiagramNode[];

    constructor(nodesMap?: Map<DiagramNode>, linksMap?: Map<Link>, robotsDiagramNode?: RobotsDiagramNode,
                subprogramDiagramNodes?: SubprogramDiagramNode[]);

}

declare class DiagramEditor {

    constructor();
    public getGraph(): joint.dia.Graph;
    public getPaper(): DiagramPaper;
    public clear(): void;

}

declare interface Command {

    execute(): void;
    revert(): void;
    isRevertible(): boolean;

}

declare class UndoRedoController {

    public addCommand(command: Command);
    public undo(): void;
    public redo(): void;
    public clearStack(): void;
    public bindKeyboardHandler();
    public unbindKeyboardHandler();

}

declare class Gesture {

    name: string;
    key: string[];
    factor: number;

    constructor(name : string, key : string[], factor: number)

}

declare class HtmlView {

    protected content: string;

    public getContent(): string;

}

declare class SubprogramPaletteView extends HtmlView {
    constructor(subprogramDiagramNodes: SubprogramDiagramNode[], subprogramImageSrc: string);
}

declare class BlocksPaletteView extends HtmlView {
    constructor(paletteTypes: PaletteTypes);
}

declare class CategoryView extends HtmlView {
    constructor(categoryName: string, category: Map<NodeType>);
}

declare class CheckboxPropertyView extends HtmlView {
    constructor(typeName: string, propertyKey: string, property: Property);
}

declare class DropdownPropertyView extends HtmlView {
    constructor(typeName: string, propertyKey: string, property: Property);
}

declare class PaletteElementView extends HtmlView {
    constructor(typeName: string, name: string, imageSrc: string);
}

declare class SpinnerPropertyView extends HtmlView {
    constructor(propertyKey: string, property: Property);
}

declare class StringPropertyView extends HtmlView {
    constructor(propertyKey: string, property: Property);
}

declare class SubprogramPaletteElementView extends HtmlView {
    constructor(typeName: string, name: string, imageSrc: string, nodeLogicalId: string);
}

declare abstract class DiagramEditorController {

    protected scope: ng.IScope;
    protected diagramEditor: DiagramEditor;
    protected paperController: PaperController;
    protected propertyEditorController: PropertyEditorController;
    protected elementsTypeLoader: ElementsTypeLoader;
    protected paletteController: PaletteController;
    protected nodeTypesMap: Map<NodeType>;
    protected undoRedoController: UndoRedoController;

    constructor($scope, $attrs);

    public getGraph(): joint.dia.Graph;
    public getNodesMap(): Map<DiagramNode>;
    public getLinksMap(): Map<Link>;
    public setNodeProperties(element: DiagramElement): void;
    public clearNodeProperties(): void;
    public getNodeType(type: string): NodeType;
    public getNodeProperties(type: string): Map<Property>;
    public clearState(): void;

}

declare class PaperController {

    constructor(diagramEditorController: DiagramEditorController, paper: DiagramPaper);
    public getCurrentElement(): DiagramElement;
    public clearState(): void;
    public createLink(sourceId: string, targetId: string): void;
    public createNode(type: string, x: number, y: number, subprogramId?: string, subprogramName?: string): void;
    public createNodeInEventPositionFromNames(names: string[], event): void;
    public createLinkBetweenCurrentAndEventTargetElements(event): void;
    public setCurrentElement(element: DiagramElement): void;
    public addNode(node: DiagramNode):void;
    public removeElement(element: DiagramElement): void;
    public addLink(link: Link): void;
    public changeCurrentElement(element: DiagramElement): void;

}

declare class PropertyEditorController {

    constructor(paperController: PaperController, undoRedoController: UndoRedoController);
    public setNodeProperties(element: DiagramElement): void;
    public clearState(): void;
    public setProperty(key: string, value: string): void;
    public changeHtmlElementValue(id: string, value: string): void;
    public changeCheckboxHtml(id: string, value: string): void;

}

declare class PropertyViewFactory {

    public createView(typeName: string, propertyKey: string, property: Property): HtmlView;

}

declare class ElementsTypeLoader {

    load(callback: (elementTypes: ElementTypes) => void, kit?: string, task?: string): void;

}

declare class PaletteController {

    public initDraggable(): void;
    public appendSubprogramsPalette(subprogramDiagramNodes: SubprogramDiagramNode[],
                                    nodeTypesMap: Map<NodeType>): void;
    public appendBlocksPalette(paletteTypes: PaletteTypes): void;

}

declare class DiagramJsonParser {

    public parse(diagramJson: any, nodeTypesMap: Map<NodeType>): DiagramParts;
    protected findMinPosition(diagramJson: any, nodeTypesMap: Map<NodeType>): {x: number; y: number};
    protected parseNodes(diagramJson: any, nodeTypesMap: Map<NodeType>, offsetX: number, offsetY: number): DiagramParts;
    protected parseRobotsDiagramNode(nodeObject: any): RobotsDiagramNode;
    protected parseSubprogramDiagram(nodeObject: any): SubprogramDiagramNode;
    protected parseDiagramNodeObject(nodeObject: any, nodeTypesMap: Map<NodeType>,
                                     offsetX: number, offsetY: number): DiagramNode;
    protected parseLinks(diagramJson: any, offsetX: number, offsetY: number): Map<Link>;
    protected parseLinkObject(linkObject: any, offsetX: number, offsetY: number): Link;
    protected parseVertices(configuration: string);
    protected getSourcePosition(configuration: string);
    protected getTargetPosition(configuration: string);
    protected parsePosition(position: string): {x: number; y: number};
    protected parseId(idString: string): string;

}

declare class DiagramExporter {

    public exportDiagramStateToJSON(graph: joint.dia.Graph, diagramParts: DiagramParts);
    protected exportRobotsDiagramNode(diagramParts: DiagramParts);
    protected exportNodes(graph: joint.dia.Graph, diagramParts: DiagramParts);
    protected exportLinks(diagramParts: DiagramParts);
    protected exportProperties(properties: Map<Property>);
    protected exportVertices(vertices): string;

}

declare module GesturesUtils {

    export class Pair {
        first: number;
        second: number;

        constructor(first : number, second : number);
    }

    export class PairString {
        first: string;
        second: string;

        constructor(curString: string);

        public getString(): string;
    }

}

declare class GesturesController {

    constructor(paperController: PaperController);
    public startDrawing(): void;
    public onMouseMove(event): void;
    public onMouseDown(event): void;
    public onMouseUp(event): void;

}

declare class GesturesMatcher {

    constructor(gestures: Gesture[])
    public getMatches(pointList: GesturesUtils.Pair[]): string[];

}

declare class DiagramElementListener {
    static getNodeProperties: (type: string) => Map<Property>;
}