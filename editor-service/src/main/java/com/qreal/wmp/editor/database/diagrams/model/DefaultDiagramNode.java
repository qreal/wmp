package com.qreal.wmp.editor.database.diagrams.model;

import com.qreal.wmp.thrift.gen.TDefaultDiagramNode;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Diagram's node.*/
@Data
public class DefaultDiagramNode implements Serializable {
    private String id;

    private String logicalId;

    private String graphicalId;

    private String type;

    private Set<Property> properties = new HashSet<>();

    public DefaultDiagramNode() { }

    /** Constructor-converter from Thrift TDefaultDiagramNode to DefaultDiagramNode.*/
    public DefaultDiagramNode(TDefaultDiagramNode tDefaultDiagramNode) {
        if (tDefaultDiagramNode.isSetId()) {
            id = tDefaultDiagramNode.getId();
        }

        if (tDefaultDiagramNode.isSetLogicalId()) {
            logicalId = tDefaultDiagramNode.getLogicalId();
        }

        if (tDefaultDiagramNode.isSetGraphicalId()) {
            graphicalId = tDefaultDiagramNode.getGraphicalId();
        }

        if (tDefaultDiagramNode.isSetType()) {
            type = tDefaultDiagramNode.getType();
        }

        if (tDefaultDiagramNode.isSetProperties()) {
            properties = tDefaultDiagramNode.getProperties().stream().map(Property::new).
                    collect(Collectors.toSet());
        }
    }

    /** Converter from DefaultDiagramNode to Thrift TDefaultDiagramNode.*/
    public TDefaultDiagramNode toTDefaultDiagramNode() {
        TDefaultDiagramNode tDefaultDiagramNode = new TDefaultDiagramNode();

        if (id != null) {
            tDefaultDiagramNode.setId(id);
        }

        if (logicalId != null) {
            tDefaultDiagramNode.setLogicalId(logicalId);
        }

        if (graphicalId != null) {
            tDefaultDiagramNode.setGraphicalId(graphicalId);
        }

        if (type != null) {
            tDefaultDiagramNode.setType(type);
        }

        if (properties != null) {
            tDefaultDiagramNode.setProperties(properties.stream().map(Property::toTProperty).
                    collect(Collectors.toSet()));
        }
        return tDefaultDiagramNode;
    }
}
