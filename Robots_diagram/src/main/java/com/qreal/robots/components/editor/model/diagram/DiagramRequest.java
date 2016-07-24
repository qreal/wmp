package com.qreal.robots.components.editor.model.diagram;

import java.io.Serializable;
//TODO Anybody using it?
public class DiagramRequest implements Serializable {

    public Diagram getDiagram() {
        return this.diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    private Diagram diagram;

    private Long folderId;
}
