package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.editor.model.diagram.*;
import com.qreal.robots.components.editor.thrift.gen.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EditorInterfaceConverter {
    public Diagram convertFromTDiagram(TDiagram diagram) {
        if (diagram == null)
            return null;

        Diagram newDiagram = new Diagram();
        newDiagram.setName(diagram.getName());
        if (diagram.getDiagramId() == -1)
            newDiagram.setDiagramId(null);
        else
            newDiagram.setDiagramId(diagram.getDiagramId());

        Iterator<TDefaultDiagramNode> itr = diagram.getNodes().iterator();
        Set<DefaultDiagramNode> nodes = new HashSet<DefaultDiagramNode>();
        while(itr.hasNext()) {
            TDefaultDiagramNode node = itr.next();
            DefaultDiagramNode newNode = new DefaultDiagramNode();

            newNode.setLogicalId(node.getLogicalId());
            newNode.setGraphicalId(node.getGraphicalId());
            newNode.setType(node.getType());

            Iterator<TProperty> pItr = node.getProperties().iterator();
            Set<NodeProperty> properties = new HashSet<NodeProperty>();
            while(pItr.hasNext()) {
                TProperty property = pItr.next();
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

        Iterator<TLink> itrL = diagram.getLinks().iterator();
        Set<Link> links = new HashSet<Link>();
        while (itrL.hasNext()) {
            TLink link = itrL.next();
            Link newLink = new Link();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<TProperty> pItr = link.getProperties().iterator();
            Set<LinkProperty> properties = new HashSet<LinkProperty>();
            while(pItr.hasNext()) {
                TProperty property = pItr.next();
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

    public TDiagram convertToTDiagram(Diagram diagram) {
        if (diagram == null)
            return null;

        TDiagram newDiagram = new TDiagram();
        newDiagram.setDiagramId(diagram.getDiagramId());
        newDiagram.setName(diagram.getName());

        Iterator<DefaultDiagramNode> itr = diagram.getNodes().iterator();
        Set<TDefaultDiagramNode> nodes = new HashSet<TDefaultDiagramNode>();
        while(itr.hasNext()) {
            DefaultDiagramNode node = itr.next();
            TDefaultDiagramNode newNode = new TDefaultDiagramNode();

            newNode.setLogicalId(node.getLogicalId());
            newNode.setGraphicalId(node.getGraphicalId());
            newNode.setType(node.getType());

            Iterator<NodeProperty> pItr = node.getProperties().iterator();
            Set<TProperty> properties = new HashSet<TProperty>();
            while(pItr.hasNext()) {
                NodeProperty property = pItr.next();
                TProperty newProperty = new TProperty();
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

        Iterator<Link> itrL = diagram.getLinks().iterator();
        Set<TLink> links = new HashSet<TLink>();
        while (itrL.hasNext()) {
            Link link = itrL.next();
            TLink newLink = new TLink();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<LinkProperty> pItr = link.getProperties().iterator();
            Set<TProperty> properties = new HashSet<TProperty>();
            while(pItr.hasNext()) {
                LinkProperty property = pItr.next();
                TProperty newProperty = new TProperty();
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

    public TFolder convertFolderTree(Folder node) {
        TFolder folder = new TFolder();
        folder.setFolderName(node.getFolderName());
        folder.setFolderId(node.getFolderId());
        if (node.getFolderParentId() != null)
            folder.setFolderParentId(node.getFolderParentId());

        Set<TDiagram> diagrams = new HashSet<TDiagram>();
        Iterator<Diagram> itrD = node.getDiagrams().iterator();
        while (itrD.hasNext()) {
            Diagram diagram = itrD.next();
            diagrams.add(convertToTDiagram(diagram));
        }

        Set<TFolder> children = new HashSet<TFolder>();
        Iterator<Folder> itrF = node.getChildrenFolders().iterator();
        while (itrF.hasNext()) {
            Folder child = itrF.next();
            children.add(convertFolderTree(child));
        }

        folder.setDiagrams(diagrams);
        folder.setChildrenFolders(children);

        return folder;
    }
}