package com.qreal.wmp.db.user.model.diagram;

import com.qreal.wmp.thrift.gen.TDiagram;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

/** Diagram (now only graphs).*/
@Entity
@Table(name = "diagrams")
public class Diagram implements Serializable {

    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

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

    /** Constructor-converter from Thrift TDiagram to Diagram.*/
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

    /** Converter from Diagram to Thrift TDiagram.*/
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
