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
            this.setDiagramId(tDiagram.getDiagramId());
        }

        if (tDiagram.isSetName()) {
            this.setName(tDiagram.getName());
        }

        if (tDiagram.isSetNodes()) {
            this.setNodes(tDiagram.getNodes().stream().map(DefaultDiagramNode::new).collect(Collectors.toSet()));
        }

        if (tDiagram.isSetLinks()) {
            this.setLinks(tDiagram.getLinks().stream().map(Link::new).collect(Collectors.toSet()));
        }

    }

    public TDiagram toTDiagram() {

        TDiagram tDiagram = new TDiagram();

        if (this.diagramId != null) {
            tDiagram.setDiagramId(diagramId);
        }

        if (this.name != null) {
            tDiagram.setName(name);
        }

        if (this.nodes != null) {
            tDiagram.setNodes(nodes.stream().map(DefaultDiagramNode::toTDefaultDiagramNode).collect(Collectors.toSet()));
        }

        if (this.links != null) {
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
