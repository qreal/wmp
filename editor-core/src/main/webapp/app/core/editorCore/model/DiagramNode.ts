import {PropertyEditElement} from "./PropertyEditElement";
import {DiagramElement} from "./DiagramElement";
import {DiagramContainer} from "./DiagramContainer";
import {NodeType} from "./NodeType";
import CellView = joint.dia.CellView;
export interface DiagramNode extends DiagramElement {

    getX(): number;
    getY(): number;
    getBBox(): any;
    getHeight(): number;
    getWidth(): number;
    getImagePath(): string;
    getSize(): string;
    getParentNode(): DiagramContainer;
    setPosition(x: number, y: number, zoom: number, cellView : joint.dia.CellView): void;
    setSize(width: number, height: number): void;
    setParentNode(parent: DiagramContainer, embedding?: boolean): void;
    setPreliminaryParent(parent: DiagramContainer): void;
    setNodeType(nodeType: NodeType): void;
    getPropertyEditElement(): PropertyEditElement;
    initPropertyEditElements(zoom: number): void;
    initResize(bbox, x: number, y: number, paddingPercent): void;
    completeResize(): void;
    isResizing(): boolean;
    isValidEmbedding(child: DiagramNode): boolean;
    pointermove(cellView, evt, x, y): void;

}