/// <reference path="../../../app/vendor.d.ts" />
declare module "common/constants/DefaultSize" {
    export class DefaultSize {
        static DEFAULT_NODE_WIDTH: number;
        static DEFAULT_NODE_HEIGHT: number;
    }
}
declare module "common/constants/MouseButton" {
    export enum MouseButton {
        left = 0,
        middle = 1,
        right = 2,
        browserBack = 3,
        browserForward = 4,
    }
}
declare module "core/editorCore/model/Property" {
    export class Property {
        name: string;
        type: string;
        value: string;
        constructor(name: string, type: string, value: string);
    }
}
declare module "core/editorCore/model/PropertiesPack" {
    import { Property } from "core/editorCore/model/Property";
    export class PropertiesPack {
        logical: Map<String, Property>;
        graphical: Map<String, Property>;
        constructor(logical: Map<String, Property>, graphical: Map<String, Property>);
    }
}
declare module "core/editorCore/model/DiagramElement" {
    import { Property } from "core/editorCore/model/Property";
    import { PropertiesPack } from "core/editorCore/model/PropertiesPack";
    export interface DiagramElement {
        getLogicalId(): string;
        getJointObject(): any;
        getName(): string;
        getType(): string;
        getConstPropertiesPack(): PropertiesPack;
        getChangeableProperties(): Map<String, Property>;
        setProperty(name: string, property: Property): void;
    }
}
declare module "core/editorCore/model/Variant" {
    export class Variant {
        private key;
        private value;
        constructor(key: string, value: string);
        getKey(): string;
        getValue(): string;
    }
}
declare module "core/editorCore/controller/VariantListMapper" {
    import { Variant } from "core/editorCore/model/Variant";
    export class VariantListMapper {
        private static variantsMap;
        static addVariantList(typeName: string, propertyKey: string, variants: Variant[]): void;
        static getVariantList(typeName: string, propertyKey: string): Variant[];
    }
}
declare module "core/editorCore/model/commands/Command" {
    export interface Command {
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/ChangePropertyCommand" {
    import { Command } from "core/editorCore/model/commands/Command";
    export class ChangePropertyCommand implements Command {
        private key;
        private value;
        private oldValue;
        private executionFunction;
        private changeHtmlFunction;
        constructor(key: string, value: string, oldValue: string, executionFunction: (name: string, value: string) => void, changeHtmlFunction: (value: string) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/view/HtmlView" {
    export class HtmlView {
        protected content: string;
        getContent(): string;
    }
}
declare module "utils/StringUtils" {
    export class StringUtils {
        static format(str: string, ...args: string[]): string;
        static getInputStringSize(input: any): number;
    }
}
declare module "core/editorCore/view/SpinnerPropertyView" {
    import { Property } from "core/editorCore/model/Property";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class SpinnerPropertyView extends HtmlView {
        private template;
        constructor(propertyKey: string, property: Property);
    }
}
declare module "core/editorCore/view/DropdownPropertyView" {
    import { Property } from "core/editorCore/model/Property";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class DropdownPropertyView extends HtmlView {
        private template;
        constructor(typeName: string, propertyKey: string, property: Property);
    }
}
declare module "core/editorCore/view/CheckboxPropertyView" {
    import { Property } from "core/editorCore/model/Property";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class CheckboxPropertyView extends HtmlView {
        private template;
        constructor(typeName: string, propertyKey: string, property: Property);
    }
}
declare module "core/editorCore/view/StringPropertyView" {
    import { Property } from "core/editorCore/model/Property";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class StringPropertyView extends HtmlView {
        private template;
        constructor(nodeId: string, propertyKey: string, property: Property);
    }
}
declare module "core/editorCore/service/SelectorService" {
    export class SelectorService {
        getSelectors(): any;
        constructor(selectos: string);
    }
}
declare module "core/editorCore/controller/PropertyViewFactory" {
    import { HtmlView } from "core/editorCore/view/HtmlView";
    import { Property } from "core/editorCore/model/Property";
    export class PropertyViewFactory {
        createView(nodeId: string, typeName: string, propertyKey: string, property: Property): HtmlView;
    }
}
declare module "core/editorCore/controller/UndoRedoController" {
    import { Command } from "core/editorCore/model/commands/Command";
    export class UndoRedoController {
        private stack;
        private pointer;
        private maxSize;
        private keyDownHandler;
        constructor();
        addCommand(command: Command): void;
        undo(): void;
        redo(): void;
        clearStack(): void;
        bindKeyboardHandler(): void;
        unbindKeyboardHandler(): void;
        private popNCommands(n);
    }
}
declare module "core/editorCore/model/commands/MultiCommand" {
    import { Command } from "core/editorCore/model/commands/Command";
    export class MultiCommand implements Command {
        private commands;
        constructor(commands: Command[]);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/controller/UIDGenerator" {
    export class UIDGenerator {
        static generate(): string;
    }
}
declare module "core/editorCore/model/Link" {
    import { Property } from "core/editorCore/model/Property";
    import { PropertiesPack } from "core/editorCore/model/PropertiesPack";
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    export class Link implements DiagramElement {
        private logicalId;
        private jointObject;
        private constPropertiesPack;
        private changeableProperties;
        private name;
        private type;
        constructor(jointObject: joint.dia.Link, name: string, type: string, properties: Map<String, Property>);
        getLogicalId(): string;
        getJointObject(): joint.dia.Link;
        getName(): string;
        getType(): string;
        getConstPropertiesPack(): PropertiesPack;
        getChangeableProperties(): Map<String, Property>;
        setProperty(key: string, property: Property): void;
        private changeLabel(value);
        private getDefaultConstPropertiesPack();
        private initConstLogicalProperties();
        private initConstGraphicalProperties();
        private updateHighlight();
    }
}
declare module "core/editorCore/model/PropertyEditElement" {
    import { Property } from "core/editorCore/model/Property";
    export class PropertyEditElement {
        private static propertyTemplate;
        private static template;
        private htmlElement;
        constructor(logicalId: string, jointObjectId: string, properties: Map<String, Property>);
        getHtmlElement(): any;
        setPosition(x: number, y: number): void;
        private initInputSize();
        private initInputAutosize();
    }
}
declare module "core/editorCore/model/DiagramNode" {
    import { PropertyEditElement } from "core/editorCore/model/PropertyEditElement";
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    export interface DiagramNode extends DiagramElement {
        getX(): number;
        getY(): number;
        getImagePath(): string;
        getSize(): string;
        setPosition(x: number, y: number, zoom: number, cellView: joint.dia.CellView): void;
        setSize(width: number, height: number, cellView: joint.dia.CellView): void;
        getPropertyEditElement(): PropertyEditElement;
        initPropertyEditElements(zoom: number): void;
        initResize(bbox: any, x: number, y: number, paddingPercent: any): void;
        completeResize(): void;
        isResizing(): boolean;
        pointermove(cellView: any, evt: any, x: any, y: any): void;
    }
}
declare module "core/editorCore/model/DefaultDiagramNode" {
    import { Property } from "core/editorCore/model/Property";
    import { PropertiesPack } from "core/editorCore/model/PropertiesPack";
    import { PropertyEditElement } from "core/editorCore/model/PropertyEditElement";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    export class DefaultDiagramNode implements DiagramNode {
        private logicalId;
        private jointObject;
        private name;
        private type;
        private constPropertiesPack;
        private changeableProperties;
        private imagePath;
        private propertyEditElement;
        private resizeParameters;
        private lastMousePosition;
        private boundingBox;
        constructor(name: string, type: string, x: number, y: number, width: number, height: number, properties: Map<String, Property>, imagePath: string, id?: string, notDefaultConstProperties?: PropertiesPack);
        pointermove(cellView: any, evt: any, x: any, y: any): void;
        initPropertyEditElements(zoom: number): void;
        getPropertyEditElement(): PropertyEditElement;
        getLogicalId(): string;
        getName(): string;
        getType(): string;
        getX(): number;
        getY(): number;
        getSize(): string;
        setPosition(x: number, y: number, zoom: number, cellView: joint.dia.CellView): void;
        setSize(width: number, height: number, cellView: joint.dia.CellView): void;
        getImagePath(): string;
        getJointObject(): joint.shapes.devs.ImageWithPorts;
        getConstPropertiesPack(): PropertiesPack;
        setProperty(key: string, property: Property): void;
        getChangeableProperties(): Map<String, Property>;
        private static getDefaultConstPropertiesPack(name);
        private static initConstLogicalProperties(name);
        private static initConstGraphicalProperties(name);
        private getJointObjectPagePosition(zoom);
        initResize(bbox: any, x: number, y: number, paddingPercent: any): void;
        completeResize(): void;
        isResizing(): boolean;
        private static isLeftBorderClicked(bbox, x, y, paddingPercent);
        private static isRightBorderClicked(bbox, x, y, paddingPercent);
        private static isTopBorderClicked(bbox, x, y, paddingPercent);
        private static isBottomBorderClicked(bbox, x, y, paddingPercent);
    }
}
declare module "core/editorCore/model/SubprogramNode" {
    import { PropertiesPack } from "core/editorCore/model/PropertiesPack";
    import { Property } from "core/editorCore/model/Property";
    import { DefaultDiagramNode } from "core/editorCore/model/DefaultDiagramNode";
    export class SubprogramNode extends DefaultDiagramNode {
        private subprogramDiagramId;
        private textObject;
        constructor(name: string, type: string, x: number, y: number, width: number, height: number, properties: Map<String, Property>, imagePath: string, subprogramDiagramId: string, id?: string, notDefaultConstProperties?: PropertiesPack);
        getSubprogramDiagramId(): string;
        getTextObject(): joint.shapes.basic.Text;
        setPosition(x: number, y: number, zoom: number, cellView: joint.dia.CellView): void;
    }
}
declare module "core/editorCore/model/NodeType" {
    import { Property } from "core/editorCore/model/Property";
    export class NodeType {
        private name;
        private shownName;
        private propertiesMap;
        private image;
        private isVisible;
        constructor(name: string, propertiesMap: Map<String, Property>, image: string, path?: string[]);
        getName(): string;
        getShownName(): string;
        getPropertiesMap(): Map<String, Property>;
        getImage(): string;
        getVisibility(): Boolean;
        setVisibility(isVisible: Boolean): void;
    }
}
declare module "core/editorCore/controller/DiagramElementListener" {
    import { Link } from "core/editorCore/model/Link";
    import { NodeType } from "core/editorCore/model/NodeType";
    export class DiagramElementListener {
        static pointerdown: (evt, x, y) => void;
        static getNodeType: (type: string) => NodeType;
        static makeAndExecuteCreateLinkCommand: (linkObject: Link) => void;
    }
}
declare module "core/editorCore/model/DiagramScene" {
    import { Link } from "core/editorCore/model/Link";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    import { SubprogramNode } from "core/editorCore/model/SubprogramNode";
    export class DiagramScene extends joint.dia.Paper {
        private htmlId;
        private graph;
        private currentLinkType;
        private nodesMap;
        private linksMap;
        private linkPatternsMap;
        private gridSize;
        private zoom;
        constructor(id: string, graph: joint.dia.Graph);
        getId(): string;
        getGridSize(): number;
        getZoom(): number;
        getNodesMap(): Map<String, DiagramNode>;
        getLinksMap(): Map<String, Link>;
        getNodeById(id: string): DiagramNode;
        getLinkById(id: string): Link;
        addNodesFromMap(nodesMap: Map<String, DiagramNode>): void;
        addLinksFromMap(linksMap: Map<String, Link>): void;
        addLinkToMap(link: Link): void;
        addLinkToPaper(link: Link): void;
        removeNode(nodeId: string): void;
        getConnectedLinkObjects(node: DiagramNode): Link[];
        removeLink(linkId: string): void;
        clear(): void;
        addSubprogramNode(node: SubprogramNode): void;
        addNode(node: DiagramNode): void;
        setCurrentLinkType(linkType: string): void;
        getCurrentLinkType(): joint.dia.Link;
        getCurrentLinkTypeName(): string;
        setLinkPatterns(linkPatterns: Map<String, joint.dia.Link>): void;
        private addLink(link);
    }
}
declare module "core/editorCore/model/commands/MoveCommand" {
    import { Command } from "core/editorCore/model/commands/Command";
    export class MoveCommand implements Command {
        private oldX;
        private oldY;
        private newX;
        private newY;
        private zoom;
        private cellView;
        private executionFunction;
        constructor(oldX: number, oldY: number, newX: number, newY: number, zoom: number, cellView: joint.dia.CellView, executionFunction: (x: number, y: number, zoom: number, cellView: joint.dia.CellView) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/ResizeCommand" {
    import { Command } from "core/editorCore/model/commands/Command";
    export class ResizeCommand implements Command {
        private oldWidth;
        private oldHeight;
        private newWidth;
        private newHeight;
        private cellView;
        private executionFunction;
        constructor(oldWidth: number, oldHeight: number, newWidth: number, newHeight: number, cellView: joint.dia.CellView, executionFunction: (x: number, y: number, cellView: joint.dia.CellView) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/RemoveElementCommand" {
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { Command } from "core/editorCore/model/commands/Command";
    export class RemoveElementCommand implements Command {
        private element;
        private executionFunction;
        private revertFunction;
        constructor(element: DiagramElement, executionFunction: (element: DiagramElement) => void, revertFunction: (element: DiagramElement) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/CreateElementCommand" {
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { Command } from "core/editorCore/model/commands/Command";
    export class CreateElementCommand implements Command {
        private element;
        private executionFunction;
        private revertFunction;
        constructor(element: DiagramElement, executionFunction: (element: DiagramElement) => void, revertFunction: (element: DiagramElement) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/ChangeCurrentElementCommand" {
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { Command } from "core/editorCore/model/commands/Command";
    export class ChangeCurrentElementCommand implements Command {
        private element;
        private oldElement;
        private executionFunction;
        constructor(element: DiagramElement, oldElement: DiagramElement, executionFunction: (element: DiagramElement) => void);
        execute(): void;
        revert(): void;
        isRevertible(): boolean;
    }
}
declare module "core/editorCore/model/commands/SceneCommandFactory" {
    import { Command } from "core/editorCore/model/commands/Command";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    import { Link } from "core/editorCore/model/Link";
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { SceneController } from "core/editorCore/controller/SceneController";
    export class SceneCommandFactory {
        private sceneController;
        constructor(sceneController: SceneController);
        makeChangeCurrentElementCommand(newElement: DiagramElement, oldElement: DiagramElement): Command;
        makeCreateNodeCommand(node: DiagramNode): Command;
        makeCreateLinkCommand(link: Link): Command;
        makeRemoveNodeCommand(node: DiagramNode): Command;
        makeRemoveLinkCommand(link: Link): Command;
        makeMoveCommand(node: DiagramNode, oldX: number, oldY: number, newX: number, newY: number, zoom: number, cellView: joint.dia.CellView): Command;
        makeResizeCommand(node: DiagramNode, oldWidth: number, oldHeight: number, newWidth: number, newHeight: number, cellView: joint.dia.CellView): Command;
    }
}
declare module "core/editorCore/controller/SceneController" {
    import { Link } from "core/editorCore/model/Link";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    import { DiagramScene } from "core/editorCore/model/DiagramScene";
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { DiagramEditorController } from "core/editorCore/controller/DiagramEditorController";
    export class SceneController {
        private diagramEditorController;
        private scene;
        private currentElement;
        private clickFlag;
        private rightClickFlag;
        private undoRedoController;
        private lastCellMouseDownPosition;
        private lastCellMouseDownSize;
        private paperCommandFactory;
        private contextMenuId;
        constructor(diagramEditorController: DiagramEditorController, paper: DiagramScene);
        getCurrentElement(): DiagramElement;
        clearState(): void;
        createLink(sourceId: string, target: any): void;
        createNode(type: string, x: number, y: number, subprogramId?: string, subprogramName?: string): void;
        createNodeInEventPositionFromNames(names: string[], event: any): void;
        createLinkBetweenCurrentAndEventTargetElements(event: any): void;
        changeCurrentElement(element: DiagramElement): void;
        makeAndExecuteCreateLinkCommand(link: Link): void;
        setCurrentElement(element: DiagramElement): void;
        addNode(node: DiagramNode): void;
        removeElement(element: DiagramElement): void;
        addLink(link: Link): void;
        private blankPoinerdownListener(event, x, y);
        private cellPointerdownListener(cellView, event, x, y);
        private cellPointerupListener(cellView, event, x, y);
        private cellPointermoveListener(cellView, event, x, y);
        private initDropPaletteElementListener();
        private selectElement(jointObject);
        private unselectElement(jointObject);
        private initCustomContextMenu();
        private initDeleteListener();
        private removeCurrentElement();
        private initPropertyEditorListener();
        private getElementBelow(event, checker?);
    }
}
declare module "core/editorCore/controller/PropertyEditorController" {
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { UndoRedoController } from "core/editorCore/controller/UndoRedoController";
    import { SceneController } from "core/editorCore/controller/SceneController";
    export class PropertyEditorController {
        private propertyViewFactory;
        private sceneController;
        private undoRedoController;
        constructor(sceneController: SceneController, undoRedoController: UndoRedoController, propertySelectorService);
        setNodeProperties(element: DiagramElement): void;
        addChangePropertyCommand(key: string, value: string, changeHtmlFunction: () => void): void;
        clearState(): void;
        setProperty(key: string, value: string): void;
        changeHtmlElementValue(id: string, value: string): void;
        changeCheckboxHtml(id: string, value: string): void;
        private initCombobox(typeName, propertyKey, element);
        private initInputStringListener();
        private initCheckboxListener();
        private initDropdownListener();
        private initSpinnerListener();
        private changeCheckboxValue(value);
    }
}
declare module "core/editorCore/model/PaletteTree" {
    import { NodeType } from "core/editorCore/model/NodeType";
    export class PaletteTree {
        categories: Map<String, PaletteTree>;
        nodes: NodeType[];
        constructor();
        convertToMap(): Map<String, NodeType>;
    }
}
declare module "core/editorCore/model/ElementTypes" {
    import { PaletteTree } from "core/editorCore/model/PaletteTree";
    import { NodeType } from "core/editorCore/model/NodeType";
    export class ElementTypes {
        uncategorisedTypes: Map<String, NodeType>;
        blockTypes: PaletteTree;
        flowTypes: PaletteTree;
        linkPatterns: Map<String, joint.dia.Link>;
        constructor();
    }
}
declare module "core/editorCore/model/SubprogramDiagramNode" {
    import { Property } from "core/editorCore/model/Property";
    export class SubprogramDiagramNode {
        private logicalId;
        private properties;
        private type;
        private name;
        constructor(logicalId: string, name: string);
        getLogicalId(): string;
        getType(): string;
        getName(): string;
        getProperties(): Map<String, Property>;
        private initProperties(name);
    }
}
declare module "core/editorCore/model/DiagramParts" {
    import { SubprogramDiagramNode } from "core/editorCore/model/SubprogramDiagramNode";
    import { Link } from "core/editorCore/model/Link";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    export class DiagramParts {
        nodesMap: Map<String, DiagramNode>;
        linksMap: Map<String, Link>;
        subprogramDiagramNodes: SubprogramDiagramNode[];
        constructor(nodesMap?: Map<String, DiagramNode>, linksMap?: Map<String, Link>, subprogramDiagramNodes?: SubprogramDiagramNode[]);
    }
}
declare module "core/editorCore/controller/parsers/TypesParser" {
    import { ElementTypes } from "core/editorCore/model/ElementTypes";
    export class TypesParser {
        private currentProperties;
        private linkPatterns;
        private currentImage;
        parse(typesJson: any): ElementTypes;
        private parseElementsTypes(elementsTypes);
        private parseGeneralTypes(generalTypes);
        private parsePaletteTypes(paletteTypes);
        private createNodeTypes(typeObject);
        private parseSubtypes(categories, path);
        private parseTypeProperties(typeName, propertiesArrayNode);
        private addVariantList(typeName, propertyKey, variantsArrayNode);
    }
}
declare module "core/editorCore/controller/loaders/ElementsTypeLoader" {
    import { ElementTypes } from "core/editorCore/model/ElementTypes";
    export class ElementsTypeLoader {
        load(callback: (elementTypes: ElementTypes) => void, kit?: string, task?: string): void;
    }
}
declare module "core/editorCore/model/DiagramEditor" {
    import { DiagramScene } from "core/editorCore/model/DiagramScene";
    export class DiagramEditor {
        private graph;
        private scene;
        constructor();
        getGraph(): joint.dia.Graph;
        getScene(): DiagramScene;
        clear(): void;
    }
}
declare module "core/editorCore/view/PaletteElementView" {
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class PaletteElementView extends HtmlView {
        private imageWidth;
        private imageHeight;
        private template;
        constructor(typeName: string, name: string, imageSrc: string, elementClass: string);
    }
}
declare module "core/editorCore/view/CategoryView" {
    import { PaletteTree } from "core/editorCore/model/PaletteTree";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class CategoryView extends HtmlView {
        private template;
        constructor(categoryName: string, category: PaletteTree, elementClass: string);
    }
}
declare module "core/editorCore/view/BlocksPaletteView" {
    import { PaletteTree } from "core/editorCore/model/PaletteTree";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class BlocksPaletteView extends HtmlView {
        constructor(paletteTypes: PaletteTree, elementClass: string);
    }
}
declare module "core/editorCore/view/SubprogramPaletteElementView" {
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class SubprogramPaletteElementView extends HtmlView {
        private imageWidth;
        private imageHeight;
        private template;
        constructor(typeName: string, name: string, imageSrc: string, nodeLogicalId: string);
    }
}
declare module "core/editorCore/view/SubprogramPaletteView" {
    import { SubprogramDiagramNode } from "core/editorCore/model/SubprogramDiagramNode";
    import { HtmlView } from "core/editorCore/view/HtmlView";
    export class SubprogramPaletteView extends HtmlView {
        constructor(subprogramDiagramNodes: SubprogramDiagramNode[], subprogramImageSrc: string);
    }
}
declare module "core/editorCore/controller/PaletteController" {
    import { NodeType } from "core/editorCore/model/NodeType";
    import { ElementTypes } from "core/editorCore/model/ElementTypes";
    import { PaletteTree } from "core/editorCore/model/PaletteTree";
    import { SubprogramDiagramNode } from "core/editorCore/model/SubprogramDiagramNode";
    import { DiagramScene } from "core/editorCore/model/DiagramScene";
    export class PaletteController {
        private subprogramsSelector;
        private blocksSelector;
        private flowsSelector;
        initDraggable(): void;
        initClick(paper: DiagramScene): void;
        appendSubprogramsPalette(subprogramDiagramNodes: SubprogramDiagramNode[], nodeTypesMap: Map<String, NodeType>): void;
        appendBlocksPalette(paletteTypes: PaletteTree): void;
        appendFlowsPalette(paletteTypes: PaletteTree): void;
        searchPaletteReload(event: Event, elementTypes: ElementTypes, nodesTypesMap: Map<String, NodeType>): void;
        private appendPaletteContent(selector, content);
        private clearPaletteContent(selector);
    }
}
declare module "core/editorCore/controller/DiagramEditorController" {
    import { PropertyEditorController } from "core/editorCore/controller/PropertyEditorController";
    import { ElementTypes } from "core/editorCore/model/ElementTypes";
    import { DiagramParts } from "core/editorCore/model/DiagramParts";
    import { NodeType } from "core/editorCore/model/NodeType";
    import { UndoRedoController } from "core/editorCore/controller/UndoRedoController";
    import { Property } from "core/editorCore/model/Property";
    import { DiagramElement } from "core/editorCore/model/DiagramElement";
    import { Link } from "core/editorCore/model/Link";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    import { ElementsTypeLoader } from "core/editorCore/controller/loaders/ElementsTypeLoader";
    import { SceneController } from "core/editorCore/controller/SceneController";
    import { DiagramEditor } from "core/editorCore/model/DiagramEditor";
    import { PaletteController } from "core/editorCore/controller/PaletteController";
    export class DiagramEditorController {
        protected diagramEditor: DiagramEditor;
        protected sceneController: SceneController;
        protected propertyEditorController: PropertyEditorController;
        protected elementsTypeLoader: ElementsTypeLoader;
        protected paletteController: PaletteController;
        protected nodeTypesMap: Map<String, NodeType>;
        protected linkPatternsMap: Map<String, joint.dia.Link>;
        protected undoRedoController: UndoRedoController;
        protected elementTypes: ElementTypes;
        constructor($scope: any, $attrs: any, selectorService: any);
        getGraph(): joint.dia.Graph;
        getNodesMap(): Map<String, DiagramNode>;
        getLinksMap(): Map<String, Link>;
        setNodeProperties(element: DiagramElement): void;
        clearNodeProperties(): void;
        getNodeType(type: string): NodeType;
        getNodeProperties(type: string): Map<String, Property>;
        getUndoRedoController(): UndoRedoController;
        clearState(): void;
        getDiagramParts(): DiagramParts;
        getNodeTypes(): Map<String, NodeType>;
        getLinkPatterns(): Map<String, joint.dia.Link>;
        addFromMap(diagramParts: DiagramParts): void;
        protected handleLoadedTypes(elementTypes: ElementTypes): void;
    }
}
declare module "core/editorCore/controller/exporters/DiagramExporter" {
    import { Property } from "core/editorCore/model/Property";
    import { DiagramParts } from "core/editorCore/model/DiagramParts";
    export class DiagramExporter {
        exportDiagramStateToJSON(graph: joint.dia.Graph, diagramParts: DiagramParts): {
            'nodes': any[];
            'links': any[];
        };
        protected exportNodes(graph: joint.dia.Graph, diagramParts: DiagramParts): any[];
        protected exportLinks(diagramParts: DiagramParts): any[];
        protected exportProperties(properties: Map<String, Property>): any[];
        protected exportVertices(jointObject: any): string;
    }
}
declare module "utils/MathUtils" {
    export class MathUtils {
        static toDeg(radians: number): number;
        static toRad(degrees: number): number;
        static twoPointLenght(x1: number, y1: number, x2: number, y2: number): number;
        static sqr(x: number): number;
        static min(a: number, b: number): number;
        static toRadians(angle: number): number;
        static toDegrees(angle: number): number;
        static rotateVector(x: number, y: number, angle: number): {
            x: number;
            y: number;
        };
    }
}
declare module "core/editorCore/controller/parsers/DiagramJsonParser" {
    import { Link } from "core/editorCore/model/Link";
    import { NodeType } from "core/editorCore/model/NodeType";
    import { DiagramNode } from "core/editorCore/model/DiagramNode";
    import { SubprogramDiagramNode } from "core/editorCore/model/SubprogramDiagramNode";
    import { DiagramParts } from "core/editorCore/model/DiagramParts";
    export class DiagramJsonParser {
        parse(diagramJson: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>): DiagramParts;
        protected findMinPosition(diagramJson: any, nodeTypesMap: Map<String, NodeType>): {
            x: number;
            y: number;
        };
        protected parseNodes(diagramJson: any, nodeTypesMap: Map<String, NodeType>, offsetX: number, offsetY: number): DiagramParts;
        protected parseSubprogramDiagram(nodeObject: any): SubprogramDiagramNode;
        protected parseDiagramNodeObject(nodeObject: any, nodeTypesMap: Map<String, NodeType>, offsetX: number, offsetY: number): DiagramNode;
        protected parseLinks(diagramJson: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>, offsetX: number, offsetY: number): Map<String, Link>;
        protected parseLinkObject(linkObject: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>, offsetX: number, offsetY: number): Link;
        protected parseVertices(configuration: string): any[];
        protected getSourcePosition(configuration: string): {
            x: number;
            y: number;
        };
        protected getTargetPosition(configuration: string): {
            x: number;
            y: number;
        };
        protected parsePosition(position: string): {
            x: number;
            y: number;
        };
        protected parseSize(size: string): {
            width: number;
            height: number;
        };
        protected parseId(idString: string): string;
    }
}
