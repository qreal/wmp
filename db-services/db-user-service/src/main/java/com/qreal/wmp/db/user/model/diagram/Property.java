package com.qreal.wmp.db.user.model.diagram;

import com.qreal.wmp.thrift.gen.TProperty;
import lombok.Data;

import java.io.Serializable;

/** Property of an entity.*/
@Data
public class Property implements Serializable {
    private String propertyId;

    private String name;

    private String value;

    private String type;

    public Property() {
    }

    /** Constructor-converter from Thrift TProperty to Property.*/
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

    /** Converter from Property to Thrift TProperty.*/
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
