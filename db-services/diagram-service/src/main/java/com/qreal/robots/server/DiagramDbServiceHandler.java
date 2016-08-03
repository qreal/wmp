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

    private DiagramDao diagramDao;

    public DiagramDbServiceHandler(AbstractApplicationContext context) {
        diagramDao = (DiagramDao) context.getBean("diagramDao");
        assert diagramDao != null;
    }

    @Override
    public long saveDiagram(TDiagram diagram) {
        return diagramDao.saveDiagram(new Diagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramID) {
        Diagram diagram = diagramDao.openDiagram(diagramID);
        return diagram != null ? diagram.toTDiagram() : null;
    }

    @Override
    public void deleteDiagram(long diagramId) {
        diagramDao.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) {
        diagramDao.rewriteDiagram(new Diagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder) {
        return diagramDao.createFolder(new Folder(folder));
    }

    @Override
    public void deleteFolder(long folderId) {
        diagramDao.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username) {
        Folder folder = diagramDao.getFolderTree(username);

        return folder != null ? folder.toTFolder() : null;
    }
}
