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

    private EditorInterfaceConverter converter;

    public DiagramDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
        converter = new EditorInterfaceConverter();
    }

    @Override
    public long saveDiagram(TDiagram diagram) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        return diagramDAO.saveDiagram(converter.convertFromTDiagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramID) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        return converter.convertToTDiagram(diagramDAO.openDiagram(diagramID));
    }

    @Override
    public void deleteDiagram(long diagramId) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.rewriteDiagram(converter.convertFromTDiagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        Folder newFolder = converter.convertFromTFolder(folder);
        return diagramDAO.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        diagramDAO.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username) throws TException {
        DiagramDAO diagramDAO = (DiagramDAO) context.getBean("DiagramDAO");
        Folder folder = diagramDAO.getFolderTree(username);
        TFolder tFolder = converter.convertToTFolder(folder);
        return tFolder;
    }
}
