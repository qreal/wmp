package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
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
    Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException;

    /** Returns diagram with specified id.*/
    @NotNull Diagram getDiagram(Long diagramId) throws NotFoundException;

    /** Checks whether diagram with specified id exists.*/
    boolean isExistsDiagram(Long diagramId);

    /**
     * Rewrites diagram with id equal to <code>diagram.id</code>.
     *
     * @param diagram diagram to rewrite (Id must be set correctly).
     */
    void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException;

    /** Deletes diagram with specified id.*/
    void deleteDiagram(Long diagramId) throws AbortedException;

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long saveFolder(@NotNull Folder folder) throws AbortedException;

    /** Checks whether folder with specified id exists.*/
    boolean isExistsFolder(Long folderId);

    /** Deletes folder with specified id.*/
    void deleteFolder(Long folderId) throws AbortedException;

    /** Returns folder with specified id.*/
    @NotNull Folder getFolder(Long folderId) throws NotFoundException;

    /** Returns root folder of user.*/
    @NotNull Folder getFolderTree(String userName) throws NotFoundException;
}
