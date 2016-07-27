package com.qreal.robots.components.database.diagrams.service.client;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

/**
 * DiagramDBService interface.
 */
public interface DiagramService {

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     * @return new id of diagram
     */
    Long saveDiagram(Diagram diagram, Long folderId);

    /**
     * Returns diagram with specified id.
     */
    Diagram openDiagram(Long diagramId);

    /**
     * Rewrites diagram with id equal to <code>diagram.id</code>.
     *
     * @param diagram diagram to rewrite (<code>diagram.id</code> must be set correctly).
     */
    void rewriteDiagram(Diagram diagram);

    /**
     * Deletes diagram with specified id.
     */
    void deleteDiagram(Long diagramId);

    /**
     * Creates root folder for user with specified username.
     *
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName);

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long createFolder(Folder folder);

    /**
     * Deletes folder with specified id.
     */
    void deleteFolder(Long folderId);

    /**
     * Returns root folder of user.
     */
    Folder getFolderTree();
}
