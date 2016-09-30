package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import org.jetbrains.annotations.NotNull;

/** DAO for diagram DB. */
public interface DiagramDao {
    /**
     * Saves a diagram in specified folder and creates Id for it.
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of a folder to save the diagram into
     * @return new id of a diagram
     */
    Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException;

    /** Returns a diagram with specified id. */
    @NotNull Diagram getDiagram(Long diagramId) throws NotFoundException;

    /** Checks whether a diagram with specified id exists. */
    boolean isExistsDiagram(Long diagramId);

    /**
     * Rewrites a diagram with id equal to <code>diagram.id</code>.
     * @param diagram diagram to rewrite (Id must be set correctly).
     */
    void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException;

    /** Deletes a diagram with specified id. */
    void deleteDiagram(Long diagramId) throws AbortedException;

    /**
     * Creates a folder and assigns it an Id.
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long saveFolder(@NotNull Folder folder) throws AbortedException;

    /** Checks whether a folder with specified Id exists. */
    boolean isExistsFolder(Long folderId);

    /** Deletes a folder with specified Id. */
    void deleteFolder(Long folderId) throws AbortedException;

    /** Returns a folder with specified Id.*/
    @NotNull Folder getFolder(Long folderId) throws NotFoundException;

    /** Returns user's root folder. */
    @NotNull Folder getFolderTree(String userName) throws NotFoundException;
}
