package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TDefaultDiagramNode;
import com.qreal.robots.thrift.gen.TLink;
import com.qreal.robots.thrift.gen.TProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "diagrams")
public class Diagram implements Serializable {

    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long diagramId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<DefaultDiagramNode> nodes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<Link> links;

    public Diagram() {
    }

    public Diagram(TDiagram tDiagram) {

        if (tDiagram.isSetDiagramId()) {
            diagramId = tDiagram.getDiagramId();
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

    public TDiagram toTDiagram() {

        TDiagram tDiagram = new TDiagram();

        if (diagramId != null) {
            tDiagram.setDiagramId(diagramId);
        }

        if (name != null) {
            tDiagram.setName(name);
        }

        if (nodes != null) {
            tDiagram.setNodes(nodes.stream().map(DefaultDiagramNode::toTDefaultDiagramNode).collect(Collectors.toSet()));
        }

        if (links != null) {
            tDiagram.setLinks(links.stream().map(Link::toTLink).collect(Collectors.toSet()));
        }

        return tDiagram;
    }

    public Long getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(Long diagramId) {
        this.diagramId = diagramId;
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
