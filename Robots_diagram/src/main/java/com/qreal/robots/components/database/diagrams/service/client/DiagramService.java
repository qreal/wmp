package com.qreal.robots.components.database.diagrams.service.client;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

//In service transactions are added to DAO. DAO only DB functions implements.

public interface DiagramService {

    //todo diagram must not contain id
    Long saveDiagram(Diagram diagram, Long folderId);

    Diagram openDiagram(Long diagramId);

    //todo diagram must contain id (maybe add parameter diagramId)
    void rewriteDiagram(Diagram diagram);

    void deleteDiagram(Long diagramId);

    void createRootFolder(String userName);

    //todo folder must not contain id
    Long createFolder(Folder folder);

    void deleteFolder(Long folderId);

    Folder getFolderTree();
}
