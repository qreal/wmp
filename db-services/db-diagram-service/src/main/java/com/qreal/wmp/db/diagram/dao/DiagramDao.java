package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** DAO for diagram DB.*/
public interface DiagramDao {

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     * @return new id of diagram
     */
    Long saveDiagram(@NotNull Diagram diagram, Long folderId);

    /** Returns diagram with specified id. (or null)*/
    @Nullable
    Diagram openDiagram(Long diagramId);

    /**
     * Rewrites diagram with id equal to <code>diagram.id</code>.
     *
     * @param diagram diagram to rewrite (<code>diagram.id</code> must be set correctly).
     */
    void rewriteDiagram(@NotNull Diagram diagram);

    /** Deletes diagram with specified id.*/
    void deleteDiagram(Long diagramId);

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long createFolder(@NotNull Folder folder);

    /** Deletes folder with specified id.*/
    void deleteFolder(Long folderId);

    /** Returns folder with specified id. (or null)*/
    @Nullable Folder getFolder(Long folderId);

    /** Returns root folder of user. (or null)*/
    @Nullable Folder getFolderTree(String userName);
}
