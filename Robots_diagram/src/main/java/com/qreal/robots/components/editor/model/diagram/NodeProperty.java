package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.thrift.gen.TProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "node_properties")
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

    public NodeProperty(TProperty tProperty) {
        if (tProperty.isSetPropertyId()) {
            this.propertyId = tProperty.getPropertyId();
        }

        if (tProperty.isSetName()) {
            this.name = tProperty.getName();
        }

        if (tProperty.isSetValue()) {
            this.value = tProperty.getValue();
        }

        if (tProperty.isSetType()) {
            this.type = tProperty.getType();
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
