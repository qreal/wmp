import {Link} from "./Link";
import {DiagramNode} from "./DiagramNode";
import {SubprogramNode} from "./SubprogramNode";
import {DiagramElementListener} from "../controller/DiagramElementListener";
import {DiagramContainer} from "./DiagramContainer";
import {MapUtils} from "../../../utils/MapUtils";
export class DiagramScene extends joint.dia.Paper {

    private htmlId: string;
    private graph: joint.dia.Graph;
    private currentLinkType: string;
    private nodesMap: Map<string, DiagramNode>;
    private linksMap: Map<string, Link>;
    private linkPatternsMap: Map<string, joint.dia.Link>;
    private gridSize: number;
    private zoom: number;

    constructor(id: string, graph: joint.dia.Graph) {
        var htmlId = id;
        var gridSize = 25;
        var zoomAttr: number = parseFloat($("#" + htmlId).attr("zoom"));
        var nodesMap = new Map<string, DiagramNode>();

        super({
            el: $('#' + htmlId),
            width: 2000,
            height: 2000,
            model: graph,
            gridSize: gridSize,
            embeddingMode: true,
            frontParentOnly: false,
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
                return nodesMap.get(parentView.model.id).isValidEmbedding(nodesMap.get(childView.model.id));
            },
            elementView: joint.dia.ElementView.extend(jQuery.extend(joint.shapes.basic.PortsViewInterface,
                {
                    pointerdown: DiagramElementListener.pointerdown
                }))
        });

        this.linkPatternsMap = new Map<string, joint.dia.Link>();

        this.htmlId = htmlId;
        this.gridSize = gridSize;
        this.zoom = (zoomAttr) ? zoomAttr : 1;
        this.graph = graph;
        this.nodesMap = nodesMap;
        this.linksMap = new Map<string, Link>();
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

    public getNodesMap(): Map<string, DiagramNode> {
        return this.nodesMap;
    }

    public getLinksMap(): Map<string, Link> {
        return this.linksMap;
    }

    public getNodeById(id: string): DiagramNode {
        return this.nodesMap.get(id);
    }

    public getLinkById(id: string): Link {
        return this.linksMap.get(id);
    }

    public addNodesFromMap(nodesMap: Map<string, DiagramNode>): void {
        MapUtils.extend(this.nodesMap, nodesMap);
        for (var node of nodesMap.values()) {
            if (node instanceof SubprogramNode) {
                this.addSubprogramNode(<SubprogramNode> node);
            } else {
                this.addNode(node);
            }
        }
        for (var node of nodesMap.values()) {
            node.setParentNode(node.getParentNode(), true);
        }
    }

    public addLinksFromMap(linksMap: Map<string, Link>): void {
        MapUtils.extend(this.linksMap, linksMap);
        for (var link of linksMap.values()) {
            this.addLink(link);
        }
    }

    public addLinkToMap(link: Link): void {
        this.linksMap.set(link.getJointObject().id, link);
    }

    public addLinkToPaper(link: Link): void {
        this.addLink(link);
        this.addLinkToMap(link);
    }

    public removeNode(nodeId: string): void {
        var node: DiagramNode = this.nodesMap.get(nodeId);

        var links = this.graph.getConnectedLinks(node.getJointObject(), { inbound: true, outbound: true });

        links.forEach((link) => {
            this.linksMap.delete(link.id);
        });

        node.getJointObject().remove();
        if (node.getPropertyEditElement()) {
            node.getPropertyEditElement().getHtmlElement().remove();
        }
        this.nodesMap.delete(nodeId);
    }

    public getConnectedLinkObjects(node: DiagramNode): Link[] {
        if (!node)
            return [];
        var links = this.graph.getConnectedLinks(node.getJointObject(), { inbound: true, outbound: true });
        var linkObjects: Link[] = [];

        links.forEach((link) => linkObjects.push(this.linksMap.get(link.id)));
        return linkObjects;
    }

    public removeLink(linkId: string): void {
        var link: Link = this.linksMap.get(linkId);
        link.getJointObject().remove();
        this.linksMap.delete(linkId);
    }

    public clear(): void {
        for (var nodeId of this.nodesMap.keys()) {
            this.removeNode(nodeId);
        }
        this.linksMap = new Map<string, Link>();
    }

    public addSubprogramNode(node: SubprogramNode): void {
        var textObject: joint.shapes.basic.Text = node.getTextObject();
        node.getJointObject().embed(textObject);
        this.graph.addCell(textObject);
        this.addNode(node);
    }

    public addNode(node: DiagramNode): void {
        if (this.graph.getCell(node.getJointObject().id))
            return;
        this.nodesMap.set(node.getJointObject().id, node);
        this.graph.addCell(node.getJointObject());
        node.initPropertyEditElements(this.zoom);
        if (node.getPropertyEditElement()) {
            node.getPropertyEditElement().getHtmlElement().insertBefore("#" + this.getId());
        }
        if (node instanceof DiagramContainer) {
            node.getChildrenNodes().forEach(child => this.addNode(child));
        }
    }

    public setCurrentLinkType(linkType: string): void {
        this.currentLinkType = linkType;
    }

    public getLinkPattern(type: string): joint.dia.Link {
        return <joint.dia.Link> this.linkPatternsMap.get(type).clone();
    }

    public getCurrentLinkPattern(): joint.dia.Link {
        return this.getLinkPattern(this.currentLinkType);
    }

    public getCurrentLinkTypeName(): string {
        return this.currentLinkType;
    }

    public setLinkPatterns(linkPatterns: Map<string, joint.dia.Link>): void {
        this.linkPatternsMap = linkPatterns;
        this.currentLinkType = MapUtils.getFirstKey(linkPatterns);
    }

    private addLink(link: Link): void {
        this.graph.addCell(link.getJointObject());
    }
}