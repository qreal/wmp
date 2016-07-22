package com.qreal.robots.components.database.diagrams.service.client;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

//In service transactions are added to DAO. DAO only DB functions implements.

public interface DiagramService {

    Long saveDiagram(Diagram diagram, Long folderId);

    Diagram openDiagram(Long diagramId);

    void rewriteDiagram(Diagram diagram);

    void deleteDiagram(Long diagramId);

    void createRootFolder(String userName);

    Long createFolder(Folder folder);

    void deleteFolder(Long folderId);

    Folder getFolderTree();
}
