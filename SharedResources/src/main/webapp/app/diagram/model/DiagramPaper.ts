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

/// <reference path="Map.ts" />
/// <reference path="DiagramNode.ts" />
/// <reference path="Link.ts" />
/// <reference path="../controller/DiagramElementListener.ts" />
/// <reference path="../../vendor.d.ts" />

class DiagramPaper extends joint.dia.Paper {

    private htmlId: string;
    private graph: joint.dia.Graph;
    private nodesMap: Map<DiagramNode>;
    private linksMap: Map<Link>;
    private gridSize: number;
    private zoom: number;

    constructor(id: string, graph: joint.dia.Graph) {
        this.htmlId = id;
        this.graph = graph;
        this.nodesMap = {};
        this.linksMap = {};
        this.gridSize = 25;
        var zoomAttr: number = parseFloat($("#" + this.htmlId).attr("zoom"));
        this.zoom = (zoomAttr) ? zoomAttr : 1;

        super({
            el: $('#' + this.htmlId),
            width: 2000,
            height: 2000,
            model: graph,
            gridSize: this.gridSize,
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
            diagramElementView: joint.dia.ElementView.extend(this.getDiagramElementView())
        });

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

    public getNodesMap(): Map<DiagramNode> {
        return this.nodesMap;
    }

    public getLinksMap(): Map<Link> {
        return this.linksMap;
    }

    public getNodeById(id: string): DiagramNode {
        return this.nodesMap[id];
    }

    public getLinkById(id: string): Link {
        return this.linksMap[id];
    }

    public addNodesFromMap(nodesMap: Map<DiagramNode>): void {
        $.extend(this.nodesMap, nodesMap);
        for (var nodeId in nodesMap) {
            var node: DiagramNode = nodesMap[nodeId];
            if (node instanceof SubprogramNode) {
                this.addSubprogramNode(<SubprogramNode> node);
            } else {
                this.addNode(node);
            }
        }
    }

    public addLinksFromMap(linksMap: Map<Link>): void {
        $.extend(this.linksMap, linksMap);
        for (var linkId in linksMap) {
            var link: Link = linksMap[linkId];
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
        var node: DiagramNode = this.nodesMap[nodeId];

        var links = this.graph.getConnectedLinks(node.getJointObject(), { inbound: true, outbound: true });

        links.forEach((link) => {
            delete this.linksMap[link.id];
        });

        node.getJointObject().remove();
        if (node.getPropertyEditElement()) {
            node.getPropertyEditElement().getHtmlElement().remove();
        }
        delete this.nodesMap[nodeId];
    }
    
    public getConnectedLinkObjects(node: DiagramNode): Link[] {
        var links = this.graph.getConnectedLinks(node.getJointObject(), { inbound: true, outbound: true });
        var linkObjects: Link[] = [];
        
        links.forEach((link) => linkObjects.push(this.linksMap[link.id]));
        return linkObjects;
    }

    public removeLink(linkId: string): void {
        var link: Link = this.linksMap[linkId];
        link.getJointObject().remove();
        delete this.linksMap[linkId];
    }

    public clear(): void {
        for (var node in this.nodesMap) {
            this.removeNode(node);
        }
        this.linksMap = {};
    }

    public addSubprogramNode(node: SubprogramNode): void {
        var textObject: joint.shapes.basic.Text = node.getTextObject();
        node.getJointObject().embed(textObject);
        this.graph.addCell(textObject);
        this.addNode(node);
    }

    public addNode(node: DiagramNode): void {
        this.nodesMap[node.getJointObject().id] = node;
        this.graph.addCell(node.getJointObject());
        node.initPropertyEditElements(this.zoom);
        if (node.getPropertyEditElement()) {
            node.getPropertyEditElement().getHtmlElement().insertBefore("#" + this.getId());
        }
    }

    private addLink(link: Link): void {
        this.graph.addCell(link.getJointObject());
    }

    private getDiagramElementView() {
        return jQuery.extend(joint.shapes.basic.PortsViewInterface,
            {
                pointerdown: DiagramElementListener.pointerdown
            });
    }
}