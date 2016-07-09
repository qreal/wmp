package com.qreal.robots.components.editor.model.diagram;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "diagrams")
public class Diagram implements Serializable {

    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagramId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<DefaultDiagramNode> nodes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<Link> links;

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
