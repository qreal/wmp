package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TDiagram;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

/** Diagram (now only graphs). */
@Entity
@Table(name = "diagrams")
@Data
@EqualsAndHashCode(exclude = "updateTime")
@ToString(exclude = "updateTime")
public class Diagram implements Serializable {
    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<DefaultDiagramNode> nodes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<Link> links = new HashSet<>();

    @Column(name = "update_time")
    private long updateTime;

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

        updateTime = System.currentTimeMillis();
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

        if (nodes != null && !nodes.isEmpty()) {
            tDiagram.setNodes(nodes.stream().map(DefaultDiagramNode::toTDefaultDiagramNode).
                    collect(Collectors.toSet()));
        }

        if (links != null && !links.isEmpty()) {
            tDiagram.setLinks(links.stream().map(Link::toTLink).collect(Collectors.toSet()));
        }

        return tDiagram;
    }
}
