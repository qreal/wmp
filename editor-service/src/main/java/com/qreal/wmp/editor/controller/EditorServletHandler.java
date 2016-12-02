package com.qreal.wmp.editor.controller;

import com.qreal.wmp.editor.database.diagrams.client.DiagramService;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.palettes.client.PaletteService;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.thrift.gen.EditorServiceThrift;
import com.qreal.wmp.thrift.gen.TDiagram;
import com.qreal.wmp.thrift.gen.TFolder;
import com.qreal.wmp.thrift.gen.TPalette;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Thrift EditorRest controller.
 * RPC functions for diagrams: saveDiagram, rewriteDiagram, deleteDiagram,
 * openDiagram;
 * RPC functions for folders: createFolder, deleteFolder, getFolderTree
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
     * @param tDiagram diagram to save (Id must not be set)
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
            diagramService.rewriteDiagram(newDiagram);
        } catch (AbortedException e) {
            //TODO Here we should  send exception to client side.
            logger.error("rewriteDiagram method encountered exception Aborted. Diagram was not rewrote.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("rewriteDiagram method encountered exception ErrorConnection. Diagram was not rewrote.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    /**
     * Deletes diagram with specified id.
     */
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
     * Returns diagram with specified id.
     */
    @Override
    public TDiagram openDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        TDiagram result = null;
        try {
            Diagram diagram = diagramService.openDiagram(diagramId);
            result = diagram.toTDiagram();
        } catch (NotFoundException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("openDiagram method encountered exception NotFound. Instead of diagram will be returned null" +
                    ".", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("openDiagram method encountered exception ErrorConnection. Instead of diagram will be " +
                    "returned null.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return result;
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
            id = diagramService.createFolder(newFolder);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createFolder method encountered exception Aborted. Instead of folderId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createFolder method encountered exception ErrorConnection. Instead of folderId will be " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return id;
    }

    /**
     * Deletes folder with specified id.
     */
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

    /**
     * Returns root folder of user.
     */
    @Override
    public TFolder getFolderTree() {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        TFolder result = null;
        try {
            Folder folder = diagramService.getFolderTree();
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
    public long createPalette(TPalette palette) {
        PaletteService paletteService = (PaletteService) context.getBean("paletteService");
        long id = 0;
        Palette newPalette = new Palette(palette);
        try {
            id = paletteService.createPalette(newPalette);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createPalette method encountered exception Aborted. Instead of paletteId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createPalette method encountered exception ErrorConnection. Instead of paletteId will be " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return id;
    }
}
