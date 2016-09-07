package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.exceptions.Aborted;
import com.qreal.wmp.db.diagram.exceptions.NotFound;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import org.jetbrains.annotations.NotNull;

/** DAO for diagram DB.*/
public interface DiagramDao {

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     * @return new id of diagram
     */
    Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws Aborted;

    /** Returns diagram with specified id.*/
    @NotNull Diagram openDiagram(Long diagramId) throws NotFound;

    /** Checks whether diagram with specified id exists.*/
    boolean isExistsDiagram(Long diagramId);

    /**
     * Rewrites diagram with id equal to <code>diagram.id</code>.
     *
     * @param diagram diagram to rewrite (Id must be set correctly).
     */
    void rewriteDiagram(@NotNull Diagram diagram) throws Aborted;

    /** Deletes diagram with specified id.*/
    void deleteDiagram(Long diagramId) throws Aborted;

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long createFolder(@NotNull Folder folder) throws Aborted;

    /** Checks whether folder with specified id exists.*/
    boolean isExistsFolder(Long folderId);

    /** Deletes folder with specified id.*/
    void deleteFolder(Long folderId) throws Aborted;

    /** Returns folder with specified id.*/
    @NotNull Folder getFolder(Long folderId) throws NotFound;

    /** Returns root folder of user.*/
    @NotNull Folder getFolderTree(String userName) throws NotFound;
}
