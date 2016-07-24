package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.DAO.DiagramDAO;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.thrift.gen.DiagramDbService;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DiagramDbServiceHandler implements DiagramDbService.Iface {

    private AbstractApplicationContext context;


    public DiagramDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public long saveDiagram(TDiagram diagram) {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        return diagramDAO.saveDiagram(new Diagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramID){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        return diagramDAO.openDiagram(diagramID).toTDiagram();
    }

    @Override
    public void deleteDiagram(long diagramId){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        diagramDAO.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        diagramDAO.rewriteDiagram(new Diagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        Folder newFolder = new Folder(folder);
        return diagramDAO.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        diagramDAO.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("diagramDAO");
        Folder folder = diagramDAO.getFolderTree(username);
        return folder.toTFolder();
    }
}
