package com.qreal.wmp.generator.model;

import com.qreal.wmp.thrift.gen.TNodeProperty;
import lombok.Data;

import java.io.Serializable;

/** NodeProperty of an entity.*/
@Data
public class NodeProperty implements Serializable {
    private String propertyId;

    private String name;

    private String value;

    private String type;

    public NodeProperty() {
    }

    /** Constructor-converter from Thrift TNodeProperty to NodeProperty.*/
    public NodeProperty(TNodeProperty tProperty) {
        if (tProperty.isSetPropertyId()) {
            propertyId = tProperty.getPropertyId();
        }

        if (tProperty.isSetName()) {
            name = tProperty.getName();
        }

        if (tProperty.isSetValue()) {
            value = tProperty.getValue();
        }

        if (tProperty.isSetType()) {
            type = tProperty.getType();
        }
    }

    /** Converter from NodeProperty to Thrift TNodeProperty.*/
    public TNodeProperty toTNodeProperty() {
        TNodeProperty tProperty = new TNodeProperty();
        if (value != null) {
            tProperty.setValue(value);
        }

        if (name != null) {
            tProperty.setName(name);
        }

        if (type != null) {
            tProperty.setType(type);
        }

        if (propertyId != null) {
            tProperty.setPropertyId(propertyId);
        }
        return tProperty;
    }

}
