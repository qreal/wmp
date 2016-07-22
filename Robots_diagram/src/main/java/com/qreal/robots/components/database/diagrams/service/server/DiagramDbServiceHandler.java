package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.DAO.DiagramDAO;
import com.qreal.robots.components.database.diagrams.thrift.gen.DiagramDbService;
import com.qreal.robots.components.database.diagrams.thrift.gen.TDiagram;
import com.qreal.robots.components.database.diagrams.thrift.gen.TFolder;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.apache.thrift.TException;
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
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        return diagramDAO.saveDiagram(EditorInterfaceConverter.convertFromTDiagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramID){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        return EditorInterfaceConverter.convertToTDiagram(diagramDAO.openDiagram(diagramID));
    }

    @Override
    public void deleteDiagram(long diagramId){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.rewriteDiagram(EditorInterfaceConverter.convertFromTDiagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        Folder newFolder = EditorInterfaceConverter.convertFromTFolder(folder);
        return diagramDAO.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username){
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        Folder folder = diagramDAO.getFolderTree(username);
        TFolder tFolder = EditorInterfaceConverter.convertToTFolder(folder);
        return tFolder;
    }
}
