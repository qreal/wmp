import {Link} from "./Link";
import {DiagramNode} from "./DiagramNode";
import {SubprogramNode} from "./SubprogramNode";
import {DiagramElementListener} from "../controller/DiagramElementListener";
import {DiagramContainer} from "./DiagramContainer";
import {PropertyEditElement} from "./PropertyEditElement";
export class DiagramScene extends joint.dia.Paper {

    private htmlId: string;
    private graph: joint.dia.Graph;
    private currentLinkType: string;
    private nodesMap: Map<String, DiagramNode>;
    private linksMap: Map<String, Link>;
    private linkPatternsMap: Map<String, joint.dia.Link>;
    private gridSize: number;
    private zoom: number;

    constructor(id: string, graph: joint.dia.Graph) {
        let htmlId = id;
        let gridSize = 25;
        let zoomAttr: number = parseFloat($("#" + htmlId).attr("zoom"));
        let nodesMap = new Map<String, DiagramNode>();

        super({
            el: $('#' + htmlId),
            width: 2000,
            height: 2000,
            model: graph,
            gridSize: gridSize,
            embeddingMode: true,
            defaultLink: new joint.dia.Link({
                attrs: {
                    '.connection': { stroke: 'black' },
                    '.marker-target': { fill: 'black', d: 'M 10 0 L 0 5 L 10 10 z' }
                }
            }),
            validateConnection: function (cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
                return (!(magnetT && magnetT.getAttribute('type') === 'output')
                    && !(cellViewT && cellViewT.model.get('type') === 'link'));
            },
            validateMagnet: function (cellView, magnet) {
                return magnet.getAttribute('magnet') !== 'passive';
            },
            validateEmbedding: function(childView, parentView) {
                return nodesMap[parentView.model.id] instanceof DiagramContainer;
            },
            elementView: joint.dia.ElementView.extend(jQuery.extend(joint.shapes.basic.PortsViewInterface,
                {
                    pointerdown: DiagramElementListener.pointerdown
                }))
        });

        this.linkPatternsMap = new Map<String, joint.dia.Link>();

        this.htmlId = htmlId;
        this.gridSize = gridSize;
        this.zoom = (zoomAttr) ? zoomAttr : 1;
        this.graph = graph;
        this.nodesMap = nodesMap;
        this.linksMap = new Map<String, Link>();
        this.scale(this.zoom, this.zoom);
    }

    public getId(): string {
        return this.htmlId;
    }

    public getGridSize(): number {
        return this.gridSize;
    }

    public getZoom(): number {
        return this.zoom;
    }

    public getNodesMap(): Map<String, DiagramNode> {
        return this.nodesMap;
    }

    public getLinksMap(): Map<String, Link> {
        return this.linksMap;
    }

    public getNodeById(id: string): DiagramNode {
        return this.nodesMap[id];
    }

    public getLinkById(id: string): Link {
        return this.linksMap[id];
    }

    public addNodesFromMap(nodesMap: Map<String, DiagramNode>): void {
        $.extend(this.nodesMap, nodesMap);
        for (let nodeId in nodesMap) {
            let node: DiagramNode = nodesMap[nodeId];
            if (node instanceof SubprogramNode) {
                this.addSubprogramNode(<SubprogramNode> node);
            } else {
                this.addNode(node);
            }
        }
    }

    public addLinksFromMap(linksMap: Map<String, Link>): void {
        $.extend(this.linksMap, linksMap);
        for (let linkId in linksMap) {
            let link: Link = linksMap[linkId];
            this.addLink(link);
        }
    }

    public addLinkToMap(link: Link): void {
        this.linksMap[link.getJointObject().id] = link;
    }

    public addLinkToPaper(link: Link): void {
        this.addLink(link);
        this.addLinkToMap(link);
    }

    public removeNode(nodeId: string): void {
        let node: DiagramNode = this.nodesMap[nodeId];

        let links = this.graph.getConnectedLinks(node.getJointObject(), {inbound: true, outbound: true});

        links.forEach((link) => {
            delete this.linksMap[link.id];
        });

        node.getJointObject().remove();
        let textElements: Map<String, PropertyEditElement> = node.getPropertyEditElements();
        if (textElements !== null && typeof textElements !== 'undefined') {
            for (let propertyKey in textElements) {
                textElements[propertyKey].getTextObject().remove();
            }
        }
        delete this.nodesMap[nodeId];
    }

    public getConnectedLinkObjects(node: DiagramNode): Link[] {
        let links = this.graph.getConnectedLinks(node.getJointObject(), {inbound: true, outbound: true});
        let linkObjects: Link[] = [];

        links.forEach((link) => linkObjects.push(this.linksMap[link.id]));
        return linkObjects;
    }

    public removeLink(linkId: string): void {
        let link: Link = this.linksMap[linkId];
        link.getJointObject().remove();
        delete this.linksMap[linkId];
    }

    public clear(): void {
        for (let node in this.nodesMap) {
            this.removeNode(node);
        }
        this.linksMap = new Map<String, Link>();
    }

    public addSubprogramNode(node: SubprogramNode): void {
        let textObject: joint.shapes.basic.Text = node.getTextObject();
        node.getJointObject().embed(textObject);
        this.graph.addCell(textObject);
        this.addNode(node);
    }

    public addNode(node: DiagramNode): void {
        console.log("Diagram Scene : add node " + node);
        this.nodesMap[node.getJointObject().id] = node;
        this.graph.addCell(node.getJointObject());

        node.initPropertyEditElements(this.zoom, this.graph);
        node.getJointObject().on('change:position', function() {
            node.changeTextPosition();
        });
    }

    public setCurrentLinkType(linkType: string): void {
        this.currentLinkType = linkType;
    }

    public getCurrentLinkType(): joint.dia.Link {
        return <joint.dia.Link> this.linkPatternsMap[this.currentLinkType].clone();
    }

    public getCurrentLinkTypeName(): string {
        return this.currentLinkType;
    }

    public setLinkPatterns(linkPatterns: Map<String, joint.dia.Link>): void {
        this.linkPatternsMap = linkPatterns;
        this.currentLinkType = Object.keys(this.linkPatternsMap)[0];
    }

    private addLink(link: Link): void {
        this.graph.addCell(link.getJointObject());
    }
}