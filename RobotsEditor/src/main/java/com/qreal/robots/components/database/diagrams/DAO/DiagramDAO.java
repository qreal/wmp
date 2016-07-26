package com.qreal.robots.components.database.diagrams.DAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;

/**
 * DAO for diagram DB.
 */
public interface DiagramDAO {

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
     * Returns folder with specified id.
     */
    Folder getFolder(Long folderId);

    /**
     * Returns root folder of user.
     */
    Folder getFolderTree(String userName);
}
