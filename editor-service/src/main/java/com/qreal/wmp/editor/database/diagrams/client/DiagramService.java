package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

/** DiagramDBService interface.*/
public interface DiagramService {

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     * @return new id of diagram
     */
    Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException, ErrorConnectionException;

    /** Returns diagram with specified id.*/
    @NotNull
    Diagram openDiagram(Long diagramId) throws NotFoundException, ErrorConnectionException;

    /**
     * Rewrites diagram with id equal to diagram.id.
     *
     * @param diagram diagram to rewrite (diagram.id must be set correctly).
     */
    void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException, ErrorConnectionException;

    /**
     * Deletes diagram with specified id.
     */
    void deleteDiagram(Long diagramId) throws AbortedException, ErrorConnectionException;

    /**
     * Creates root folder for user with specified username.
     *
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName) throws AbortedException, ErrorConnectionException;

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long createFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException;

    /** Deletes folder with specified id.*/
    void deleteFolder(Long folderId) throws AbortedException, ErrorConnectionException;

    /** Returns root folder of user.*/
    @NotNull
    Folder getFolderTree() throws NotFoundException, ErrorConnectionException;
}
