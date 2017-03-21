import {AbstractBlock} from "./Blocks/AbstractBlock";
import {BlockFactory} from "./BlockFactory";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Link} from "core/editorCore/model/Link";
import {Timeline} from "../twoDModel/interfaces/engine/model/Timeline";
export class Interpreter {

    private userVariablesMap: Map<String, any>;
    private environmentVariablesMap: Map<String, any>;
    private blockFactory: BlockFactory;
    private delay: number;
    private delayTimeoutId: number;
    private stopFlag: boolean;
    private timeline: Timeline;

    constructor() {
        this.blockFactory = new BlockFactory();
    }

    public interpret(graph: joint.dia.Graph, nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>, 
                     timeline: Timeline): void {
        this.clearState();
        this.timeline = timeline;
        try {
            timeline.start();
            this.run(this.findInitialNodeId(nodesMap), graph, nodesMap, linksMap);
        } catch (error) {
            timeline.stop();
            alert(error.message);
        }
    }
    
    public addOrChangeUserVariable(name: string, value: any): void {
        this.userVariablesMap[name] = value;
    }

    public addOrChangeUserVariablesMap(variablesMap: Map<String, any>): void {
        $.extend(this.userVariablesMap, variablesMap);
    }

    public addOrChangeEnvironmentVariable(name: string, value: any): void {
        this.environmentVariablesMap[name] = value;
    }

    public addOrChangeEnvironmentVariablesMap(variablesMap: Map<String, any>): void {
        $.extend(this.environmentVariablesMap, variablesMap);
    }

    public getUserVariable(name: string): any {
        if (this.userVariablesMap[name] === undefined || this.userVariablesMap[name] === null) {
            throw new Error("No such user variable:" + name);
        }
        return this.userVariablesMap[name];
    }

    public getEnvironmentVariable(name: string): any {
        if (this.environmentVariablesMap[name] === undefined || this.environmentVariablesMap[name] === null) {
            throw new Error("No such environment variable:" + name);
        }
        return this.environmentVariablesMap[name];
    }
    
    public setDelay(delay: number): void {
        this.delay = delay;
    }

    private run(nodeId: string, graph: joint.dia.Graph, nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>): void {
        if (this.stopFlag) {
            return;
        }
        var block: AbstractBlock = this.blockFactory.createBlock(nodesMap[nodeId],
            this.getOutboundLinks(graph, nodesMap[nodeId], linksMap), this, this.timeline.getRobotModels());
        block.run();
        var nextNodeId: string = block.getNextNodeId();
        if (nextNodeId) {
            if (this.delay) {
                this.delayTimeoutId = setTimeout(() => {
                    this.delay = 0;
                    this.run(block.getNextNodeId(), graph, nodesMap, linksMap);
                }, this.delay);
            } else {
                this.run(block.getNextNodeId(), graph, nodesMap, linksMap);
            }
        } else {
            this.timeline.stop();
        }
    }
    
    public stop(): void {
        this.stopFlag = true;
        clearTimeout(this.delayTimeoutId);
        if (this.timeline) {
            this.timeline.stop();
        }
    }
    
    private getOutboundLinks(graph: joint.dia.Graph, node: DiagramNode, linksMap: Map<String, Link>): Link[] {
        var links: Link[] = [];
        var cell = graph.getCell(node.getJointObject().id);
        var jointOutboundLinks: joint.dia.Link[] = graph.getConnectedLinks(cell, { outbound : true });
        jointOutboundLinks.forEach((jointLink) => links.push(linksMap[jointLink.id]));
        return links;
    }

    private findInitialNodeId(nodesMap: Map<String, DiagramNode>): string {
        for (var id in nodesMap) {
            if (nodesMap.hasOwnProperty(id)) {
                var node: DiagramNode = nodesMap[id];
                if (node.getType() === "InitialNode") {
                    return node.getJointObject().id;
                }
            }
        }
        throw new Error("Initial node was not found");
    }

    private clearState(): void {
        this.userVariablesMap = new Map<String, any>();
        this.initEnvironmentVariables();
        this.delay = 0;
        this.stopFlag = false;
        this.delayTimeoutId = null;
        this.timeline = null;
    }

    private initEnvironmentVariables(): void {
        this.environmentVariablesMap = new Map<String, any>();
        this.environmentVariablesMap["painterColor"] = "black";
        this.environmentVariablesMap["painterWidth"] = 1;
    }

}