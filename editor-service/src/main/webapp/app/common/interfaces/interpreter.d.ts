/// <reference path="vendor.d.ts" />

declare class Interpreter {
    
    public interpret(graph: joint.dia.Graph, nodesMap: Map<DiagramNode>, linksMap: Map<Link>, timeline: Timeline);
    public stop(): void;

}