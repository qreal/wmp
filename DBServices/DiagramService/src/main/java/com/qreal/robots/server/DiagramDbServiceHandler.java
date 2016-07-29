package com.qreal.robots.server;

import com.qreal.robots.dao.DiagramDao;
import com.qreal.robots.model.Diagram;
import com.qreal.robots.model.Folder;
import com.qreal.robots.thrift.gen.DiagramDbService;
import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Thrift server side handler for DiagramDBService.
 */
@Transactional
public class DiagramDbServiceHandler implements DiagramDbService.Iface {

    private AbstractApplicationContext context;

    public DiagramDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public long saveDiagram(TDiagram diagram) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        return diagramDao.saveDiagram(new Diagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramID) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        return diagramDao.openDiagram(diagramID).toTDiagram();
    }

    @Override
    public void deleteDiagram(long diagramId) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        diagramDao.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        diagramDao.rewriteDiagram(new Diagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        Folder newFolder = new Folder(folder);
        return diagramDao.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        diagramDao.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username) {
        DiagramDao diagramDao = (DiagramDao) context.getBean("diagramDao");
        Folder folder = diagramDao.getFolderTree(username);
        return folder.toTFolder();
    }
}
