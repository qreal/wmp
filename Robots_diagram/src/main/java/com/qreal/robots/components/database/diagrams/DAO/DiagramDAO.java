package com.qreal.robots.components.database.diagrams.DAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

public interface DiagramDAO {

    Folder getFolder(Long diagramId);

    Long saveDiagram(Diagram diagram, Long folderId);

    Diagram openDiagram(Long diagramId);

    void rewriteDiagram(Diagram diagram);

    void deleteDiagram(Long diagramId);

    Long createFolder(Folder folder);

    void deleteFolder(Long folderId);

    Folder getFolderTree(String userName);
}
