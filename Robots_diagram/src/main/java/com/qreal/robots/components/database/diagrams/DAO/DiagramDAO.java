package com.qreal.robots.components.database.diagrams.DAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

public interface DiagramDAO {

    public Folder getFolder(Long diagramId);

    public Long saveDiagram(Diagram diagram, Long folderId);

    public Diagram openDiagram(Long diagramId);

    public void rewriteDiagram(Diagram diagram);

    public void deleteDiagram(Long diagramId);

    public Long createFolder(Folder folder);

    public void deleteFolder(Long folderId);

    public Folder getFolderTree(String userName);

}
