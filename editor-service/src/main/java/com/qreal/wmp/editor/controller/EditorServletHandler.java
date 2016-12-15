package com.qreal.wmp.editor.controller;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.diagrams.client.DiagramService;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.EditorServiceThrift;
import com.qreal.wmp.thrift.gen.TDiagram;
import com.qreal.wmp.thrift.gen.TFolder;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Thrift EditorRest controller.
 * RPC functions for diagrams: saveDiagram, updateDiagram, deleteDiagram, getDiagram;
 * RPC functions for folders: saveFolder, updateFolder, getFolder, deleteFolder;
 * Specific RPC functions : getFolderTree, addUserToOwnersOfFolder
 */
public class EditorServletHandler implements EditorServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    private final ApplicationContext context;

    public EditorServletHandler(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param tDiagram  diagram to saveUser (Id must not be set)
     * @return new id of diagram
     */
    @Override
    public long saveDiagram(TDiagram tDiagram) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram diagram = new Diagram(tDiagram);
        long id = 0;
        try {
            id = diagramService.saveDiagram(diagram, tDiagram.getFolderId());
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("saveDiagram method encountered exception Aborted. Instead of diagramId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("saveDiagram method encountered exception ErrorConnection. Instead of diagramId will be  " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return id;
    }

    /** Returns diagram with specified id.*/
    @Override
    public TDiagram openDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        TDiagram result = null;
        try {
            result = diagramService.getDiagram(diagramId).toTDiagram();
        } catch (NotFoundException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getDiagram method encountered exception NotFound. Instead of diagram will be returned null" +
                    ".", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getDiagram method encountered exception ErrorConnection. Instead of diagram will be " +
                    "returned null.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return result;
    }

    /**
     * Rewrites diagram with id equal to diagram.id.
     *
     * @param diagram diagram to rewrite (diagram.id must be set correctly).
     */
    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram newDiagram = new Diagram(diagram);
        try {
            diagramService.updateDiagram(newDiagram);
        } catch (AbortedException e) {
            //TODO Here we should  send exception to client side.
            logger.error("updateDiagram method encountered exception Aborted. Diagram was not rewrote.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("updateDiagram method encountered exception ErrorConnection. Diagram was not rewrote.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    /** Deletes diagram with specified id.*/
    @Override
    public void deleteDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        try {
            diagramService.deleteDiagram(diagramId);
        } catch (AbortedException e) {
            //TODO Here we should  send exception to client side.
            logger.error("deleteDiagram method encountered exception Aborted. Diagram was not deleted.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should  send exception to client side.
            logger.error("deleteDiagram method encountered exception ErrorConnection. Diagram was not deleted.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    /**
     * Creates folder and assign it id.
     *
     * @param folder folder to create (Id must not be set)
     * @return new id of folder
     */
    @Override
    public long createFolder(TFolder folder) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Folder newFolder = new Folder(folder);

        long id = 0;
        try {
            id = diagramService.saveFolder(newFolder);
            newFolder.setId(id);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("saveFolder method encountered exception Aborted. Instead of folderId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("saveFolder method encountered exception ErrorConnection. Instead of folderId will be " +
                            "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return id;
    }

    @Override
    public TFolder getFolder(long folderId, String username) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        TFolder tFolder = null;
        try {
            tFolder = diagramService.getFolder(folderId, username).toTFolder();
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getFolder method encountered exception ErrorConnection. Instead of folder will be " +
                            "returned null.", e);
        } catch (NotFoundException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("getFolder method encountered exception NotFound. Folder was not found. Instead of it null" +
                    "will be returned.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return tFolder;
    }

    @Override
    public void updateFolder(TFolder tFolder) throws TException {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        try {
            diagramService.updateFolder(new Folder(tFolder));
        } catch (ErrorConnectionException e) {
            //TODO Here we should send exception to client side.
            logger.error("updateFolder method encountered exception ErrorConnection. updateFolder() will be safely " +
                            "aborted", e);
        } catch (AbortedException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("updateFolder method encountered exception AbortedException. updateFolder() will be safely " +
                    "aborted", e);
        }
    }

    /** Deletes folder with specified id.*/
    @Override
    public void deleteFolder(long folderId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        try {
            diagramService.deleteFolder(folderId);
        } catch (AbortedException e) {
            //TODO Here we should  send exception to client side.
            logger.error("deleteFolder method encountered exception Aborted. Folder was not deleted.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should  send exception to client side.
            logger.error("deleteFolder method encountered exception ErrorConnection. Folder was not deleted.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    /** Returns root folder of user.*/
    @Override
    public TFolder getFolderTree() {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        TFolder result = null;
        try {
            String userName = AuthenticatedUser.getUserName();
            Folder folder = diagramService.getFolderTree(userName);
            result = folder.toTFolder();
        } catch (NotFoundException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getFolderTree method encountered exception NotFound. Instead of folder tree will be " +
                    "returned null.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getFolderTree method encountered exception ErrorConnection. Instead of folder tree will be " +
                    "returned null.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return result;
    }

    @Override
    public void addUserToOwners(long folderId, String username) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");

        if (username.equals(AuthenticatedUser.getUserName())) {
            logger.error("addUserToOwners method was called with name of authenticated owner. You can't share " +
                    "folder to folder tree where it already presented");
            return;
        }

        TFolder tFolderToShare = getFolder(folderId, username);
        if (tFolderToShare == null || tFolderToShare.getOwners().contains(username)) {
            return;
        }

        TFolder tFolder = getFolder(folderId, AuthenticatedUser.getUserName());

        try {
            diagramService.shareFolderTo(username, new Folder(tFolder));
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }
}
