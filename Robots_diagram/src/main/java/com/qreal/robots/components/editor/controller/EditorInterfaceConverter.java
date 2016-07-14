package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.editor.model.diagram.*;
import com.qreal.robots.components.editor.thrift.gen.DefaultDiagramNodeDAO;
import com.qreal.robots.components.editor.thrift.gen.DiagramDAO;
import com.qreal.robots.components.editor.thrift.gen.LinkDAO;
import com.qreal.robots.components.editor.thrift.gen.PropertyDAO;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EditorInterfaceConverter {
    Diagram convertDiagram(DiagramDAO diagram) {
        if (diagram == null)
            return null;

        Diagram newDiagram = new Diagram();
        newDiagram.setName(diagram.getName());
        if (diagram.getDiagramId() == -1)
            newDiagram.setDiagramId(null);
        else
            newDiagram.setDiagramId(diagram.getDiagramId());

        Iterator<DefaultDiagramNodeDAO> itr = diagram.nodes.iterator();
        Set<DefaultDiagramNode> nodes = new HashSet<DefaultDiagramNode>();
        while(itr.hasNext()) {
            DefaultDiagramNodeDAO node = itr.next();
            DefaultDiagramNode newNode = new DefaultDiagramNode();

            newNode.setLogicalId(node.getLogicalId());
            newNode.setGraphicalId(node.getGraphicalId());
            newNode.setType(node.getType());

            Iterator<PropertyDAO> pItr = node.properties.iterator();
            Set<NodeProperty> properties = new HashSet<NodeProperty>();
            while(pItr.hasNext()) {
                PropertyDAO property = pItr.next();
                NodeProperty newProperty = new NodeProperty();
                newProperty.setName(property.getName());
                newProperty.setPropertyId(property.getPropertyId());
                newProperty.setType(property.getType());
                newProperty.setValue(property.getValue());
                properties.add(newProperty);
            }
            newNode.setProperties(properties);
            nodes.add(newNode);
        }
        newDiagram.setNodes(nodes);

        Iterator<LinkDAO> itrL = diagram.links.iterator();
        Set<Link> links = new HashSet<Link>();
        while (itrL.hasNext()) {
            LinkDAO link = itrL.next();
            Link newLink = new Link();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<PropertyDAO> pItr = link.properties.iterator();
            Set<LinkProperty> properties = new HashSet<LinkProperty>();
            while(pItr.hasNext()) {
                PropertyDAO property = pItr.next();
                LinkProperty newProperty = new LinkProperty();
                newProperty.setName(property.getName());
                newProperty.setPropertyId(property.getPropertyId());
                newProperty.setType(property.getType());
                newProperty.setValue(property.getValue());
                properties.add(newProperty);
            }
            newLink.setProperties(properties);
            links.add(newLink);
        }
        newDiagram.setLinks(links);
        return newDiagram;
    }
}