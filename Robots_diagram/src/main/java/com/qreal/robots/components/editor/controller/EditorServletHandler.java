package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.thrift.gen.EditorServiceThrift;
import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift EditorRest controller.
 * RPC functions for diagrams: saveDiagram, rewriteDiagram, deleteDiagram,
 * openDiagram;
 * RPC functions for folders: createFolder, deleteFolder, getFolderTree
 */
public class EditorServletHandler implements EditorServiceThrift.Iface {

    private AbstractApplicationContext context;

    public EditorServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    /**
     * Saves diagram in specified folder and creates for it Id.
     *
     * @param tDiagram  diagram to save (Id must not be set)
     * @return new id of diagram
     */
    @Override
    public long saveDiagram(TDiagram tDiagram) throws org.apache.thrift.TException {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram diagram = new Diagram(tDiagram);
        return diagramService.saveDiagram(diagram, tDiagram.getFolderId());
    }

    /**
     * Rewrites diagram with id equal to <code>diagram.id</code>.
     *
     * @param diagram diagram to rewrite (<code>diagram.id</code> must be set correctly).
     */
    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram newDiagram = new Diagram(diagram);
        diagramService.rewriteDiagram(newDiagram);
    }

    /**
     * Deletes diagram with specified id.
     */
    @Override
    public void deleteDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        diagramService.deleteDiagram(diagramId);
    }

    /**
     * Returns diagram with specified id.
     */
    @Override
    public TDiagram openDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        return diagramService.openDiagram(diagramId).toTDiagram();
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
        return diagramService.createFolder(newFolder);
    }

    /**
     * Deletes folder with specified id.
     */
    @Override
    public void deleteFolder(long folderId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        diagramService.deleteFolder(folderId);
    }

    /**
     * Returns root folder of user.
     */
    @Override
    public TFolder getFolderTree() {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        return diagramService.getFolderTree().toTFolder();
    }

}
