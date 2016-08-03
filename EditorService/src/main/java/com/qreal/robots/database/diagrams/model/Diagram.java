package com.qreal.robots.database.diagrams.model;

import com.qreal.robots.thrift.gen.TDiagram;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diagram (now only graphs).
 */
public class Diagram implements Serializable {

    private Long id;

    private String name;

    private Set<DefaultDiagramNode> nodes;

    private Set<Link> links;

    public Diagram() {
    }

    /**
     * Constructor-converter from Thrift TDiagram to Diagram.
     */
    public Diagram(TDiagram tDiagram) {

        if (tDiagram.isSetId()) {
            id = tDiagram.getId();
        }

        if (tDiagram.isSetName()) {
            name = tDiagram.getName();
        }

        if (tDiagram.isSetNodes()) {
            nodes = tDiagram.getNodes().stream().map(DefaultDiagramNode::new).collect(Collectors.toSet());
        }

        if (tDiagram.isSetLinks()) {
            links = tDiagram.getLinks().stream().map(Link::new).collect(Collectors.toSet());
        }

    }

    /**
     * Converter from Diagram to Thrift TDiagram.
     */
    public TDiagram toTDiagram() {

        TDiagram tDiagram = new TDiagram();

        if (id != null) {
            tDiagram.setId(id);
        }

        if (name != null) {
            tDiagram.setName(name);
        }

        if (nodes != null) {
            tDiagram.setNodes(nodes.stream().map(DefaultDiagramNode::toTDefaultDiagramNode).
                    collect(Collectors.toSet()));
        }

        if (links != null) {
            tDiagram.setLinks(links.stream().map(Link::toTLink).collect(Collectors.toSet()));
        }

        return tDiagram;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNodes(Set<DefaultDiagramNode> nodes) {
        this.nodes = nodes;
    }

    public Set<DefaultDiagramNode> getNodes() {
        return nodes;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Set<Link> getLinks() {
        return links;
    }
}
