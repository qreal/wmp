package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

/** DiagramDBService interface.*/
public interface DiagramService {

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param diagram  diagram to saveUser (Id must not be set)
     * @param folderId id of folder to saveUser diagram in
     * @return new id of diagram
     */
    Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException, ErrorConnectionException,
            TException;

    /** Returns diagram with specified id.*/
    @NotNull
    Diagram getDiagram(Long diagramId) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Rewrites diagram with id equal to diagram.id.
     *
     * @param diagram diagram to rewrite (diagram.id must be set correctly).
     */
    void updateDiagram(@NotNull Diagram diagram) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Deletes diagram with specified id.
     */
    void deleteDiagram(Long diagramId) throws AbortedException, ErrorConnectionException, TException;


    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    Long saveFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException, TException;

    /** Returns folder with specified id.*/
    Folder getFolder(Long folderId) throws NotFoundException, ErrorConnectionException, TException;

    /**
     * Updates folder with id equal to folder.id.
     *
     * @param folder folder to rewrite (folder.id must be set correctly)
     */
    void updateFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException, TException;

    /** Deletes folder with specified id.*/
    void deleteFolder(Long folderId) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Creates root folder for user with specified username.
     *
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName) throws AbortedException, ErrorConnectionException, TException;

    @NotNull Folder getFolderTree(String username) throws NotFoundException, ErrorConnectionException, TException;
}
