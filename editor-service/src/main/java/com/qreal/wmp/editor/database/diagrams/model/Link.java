package com.qreal.wmp.editor.database.diagrams.model;

import com.qreal.wmp.thrift.gen.TLink;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Link between nodes.*/
@Data
public class Link implements Serializable {
    private String id;

    private String logicalId;

    private String graphicalId;

    private String type;

    private Set<Property> properties = new HashSet<>();

    public Link() { }

    /** Constructor-converter from Thrift TLink to Link.*/
    public Link(TLink tLink) {

        if (tLink.isSetId()) {
            id = tLink.getId();
        }

        if (tLink.isSetGraphicalId()) {
            graphicalId = tLink.getGraphicalId();
        }

        if (tLink.isSetLogicalId()) {
            logicalId = tLink.getLogicalId();
        }

        if (tLink.isSetType()) {
            type = tLink.getType();
        }

        if (tLink.isSetProperties()) {
            properties = tLink.getProperties().stream().map(Property::new).collect(Collectors.toSet());
        }
    }

    /** Converter from Link to Thrift TLink.*/
    public TLink toTLink() {
        TLink tLink = new TLink();

        if (id != null) {
            tLink.setId(id);
        }

        if (logicalId != null) {
            tLink.setLogicalId(logicalId);
        }

        if (graphicalId != null) {
            tLink.setGraphicalId(graphicalId);
        }

        if (type != null) {
            tLink.setType(type);
        }

        if (properties != null) {
            tLink.setProperties(properties.stream().map(Property::toTProperty).collect(Collectors.toSet()));
        }
        return tLink;
    }
}
