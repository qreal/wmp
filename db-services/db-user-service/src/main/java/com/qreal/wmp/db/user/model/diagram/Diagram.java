package com.qreal.wmp.db.user.model.diagram;

import com.qreal.wmp.thrift.gen.TDiagram;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Diagram (now only graphs).*/
@Data
public class Diagram implements Serializable {
    private Long id;

    private String name;

    private Set<DefaultDiagramNode> nodes = new HashSet<>();

    private Set<Link> links = new HashSet<>();

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
}
