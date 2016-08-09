package com.qreal.wmp.db.user.model.diagram;

import com.qreal.wmp.thrift.gen.TProperty;
import java.io.Serializable;

/** Property of a node.*/
public class Property implements Serializable {

    private String propertyId;

    private String name;

    private String value;

    private String type;

    public Property() {
    }

    /** Constructor-converter from Thrift TProperty to NodeProperty.*/
    public Property(TProperty tProperty) {
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

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /** Converter from NodeProperty to Thrift TProperty.*/
    public TProperty toTProperty() {
        TProperty tProperty = new TProperty();
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
