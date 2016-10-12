package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/** Property of an entity.*/
@Entity
@Table(name = "properties")
@Data
public class Property implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "property_id")
    private String propertyId;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "type")
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
