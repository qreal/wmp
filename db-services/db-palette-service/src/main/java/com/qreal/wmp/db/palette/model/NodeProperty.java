package com.qreal.wmp.db.palette.model;

import com.qreal.wmp.thrift.gen.TNodeProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/** NodeProperty of an entity.*/
@Entity
@Table(name = "properties")
@Data
public class NodeProperty implements Serializable {
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
    public TNodeProperty toTProperty() {
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
