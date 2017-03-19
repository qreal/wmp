import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
export abstract class AbstractBlock {

    protected node: DiagramNode;
    protected outboundLinks: Link[];
    
    constructor(node: DiagramNode, outboundLinks: Link[]) {
        this.node = node;
        this.outboundLinks = outboundLinks;
    }
    
    public abstract run(): void;
    
    public abstract getNextNodeId(): string;

    public checkExpectedNumberOfOutboundLinks(expectedNumber: number): void {
        if (this.outboundLinks.length !== expectedNumber) {
            throw new Error("There must be exact " + expectedNumber + " links from " + this.node.getName());
        }
    }
    
    public checkRangeNumberOfOutboundLinks(minNumber: number, maxNumber: number): void {
        if (this.outboundLinks.length < minNumber) {
            throw new Error("There must be at least " + minNumber + " links from " + this.node.getName());
        }
        if (this.outboundLinks.length > maxNumber) {
            throw new Error("There more than " + maxNumber + " links from " + this.node.getName());
        }
    }
    
}