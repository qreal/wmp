package com.qreal.wmp.db.diagram.repository;

//import com.qreal.wmp.db.diagram.model.DefaultDiagramNode;
//import com.qreal.wmp.db.diagram.model.Diagram;
//import com.qreal.wmp.db.diagram.model.Link;
//import com.qreal.wmp.db.diagram.model.Property;
//
//import java.util.Set;

public class DiagramsJoiner {
//    public static Diagram joinDiagramByTime(Diagram first, Diagram second, int trustedInterval) {
//        Diagram from;
//        Diagram to;
//        if (first.getUpdateTime() - second.getUpdateTime() > trustedInterval) {
//            from = first;
//            to = second;
//        }
//        else if (second.getUpdateTime() - first.getUpdateTime() > trustedInterval) {
//            from = second;
//            to = first;
//        } else {
//            return first;
//        }
//
//        to.setId(from.getId());
//        to.setName(from.getName());
//        to.setUpdateTime(from.getUpdateTime());
//        to.setNodes(joinNodes(from.getNodes(), to.getNodes()));
//        to.setLinks(joinLinks(from.getLinks(), to.getLinks()));
//
//        return to;
//    }
//
//    private static Set<Link> joinLinks(Set<Link> fromLinks, Set<Link> toLinks) {
//        for (Link link : fromLinks) {
//            if (containsLink(link, toLinks)) {
//                updateLink(link, toLinks);
//            } else {
//                toLinks.add(link);
//            }
//        }
//        return toLinks;
//    }
//
//    private static Set<DefaultDiagramNode> joinNodes(Set<DefaultDiagramNode> fromNodes,
// Set<DefaultDiagramNode> toNodes) {
//        for (DefaultDiagramNode node : fromNodes) {
//            if (containsNode(node, toNodes)) {
//                updateNode(node, toNodes);
//            } else {
//                toNodes.add(node);
//            }
//        }
//
//        return toNodes;
//    }
//
//    private static Set<Property> joinProperties(Set<Property> fromProperties, Set<Property> toProperties) {
//        for (Property property : fromProperties) {
//            if (containsProperty(property, toProperties)) {
//                updateProperties(property, toProperties);
//            } else {
//                toProperties.add(property);
//            }
//        }
//        return toProperties;
//    }
//
//    private static void updateNode(DefaultDiagramNode nodeFromUpdate, Set<DefaultDiagramNode> toNodes) {
//        DefaultDiagramNode nodeInSet = null;
//        for (DefaultDiagramNode node : toNodes) {
//            if (nodeFromUpdate.getId().equals(node.getId())) {
//                nodeInSet = node;
//                break;
//            }
//        }
//        toNodes.remove(nodeInSet);
//        nodeInSet.setId(nodeFromUpdate.getId());
//        nodeInSet.setGraphicalId(nodeFromUpdate.getGraphicalId());
//        nodeInSet.setLogicalId(nodeFromUpdate.getLogicalId());
//        nodeInSet.setType(nodeFromUpdate.getType());
//
//        nodeInSet.setProperties(joinProperties(nodeFromUpdate.getProperties(), nodeInSet.getProperties()));
//
//        toNodes.add(nodeInSet);
//    }
//
//    private static void updateLink(Link linkFromUpdate, Set<Link> toLinks) {
//        Link linkInSet = null;
//        for (Link link : toLinks) {
//            if (linkFromUpdate.getId().equals(link.getId())) {
//                linkInSet = link;
//                break;
//            }
//        }
//        toLinks.remove(linkInSet);
//        linkInSet.setId(linkFromUpdate.getId());
//        linkInSet.setGraphicalId(linkFromUpdate.getGraphicalId());
//        linkInSet.setLogicalId(linkFromUpdate.getLogicalId());
//        linkInSet.setType(linkFromUpdate.getType());
//
//        linkInSet.setProperties(joinProperties(linkFromUpdate.getProperties(), linkInSet.getProperties()));
//
//        toLinks.add(linkInSet);
//    }
//
//    private static void updateProperties(Property propertyFromUpdate, Set<Property> toProperties) {
//        Property propertyInSet = null;
//        for (Property property : toProperties) {
//            if (propertyFromUpdate.getPropertyId().equals(property.getPropertyId())) {
//                propertyInSet = property;
//                break;
//            }
//        }
//        toProperties.remove(propertyInSet);
//        propertyInSet.setPropertyId(propertyFromUpdate.getPropertyId());
//        propertyInSet.setType(propertyFromUpdate.getType());
//        propertyInSet.setName(propertyFromUpdate.getName());
//        propertyInSet.setValue(propertyFromUpdate.getValue());
//
//        toProperties.add(propertyInSet);
//    }
//
//    private static boolean containsNode(DefaultDiagramNode findNode, Set<DefaultDiagramNode> toNodes) {
//        for (DefaultDiagramNode node : toNodes) {
//            if (findNode.getId().equals(node.getId())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean containsLink(Link findLink, Set<Link> toLinks) {
//        for (Link link : toLinks) {
//            if (findLink.getId().equals(link.getId())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean containsProperty(Property findProperty, Set<Property> toProperties) {
//        for (Property property : toProperties) {
//            if (findProperty.getPropertyId().equals(property.getPropertyId())) {
//                return true;
//            }
//        }
//        return false;
//    }

}
