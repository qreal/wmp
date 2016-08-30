package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/** Thrift server side handler for DiagramDBService.*/
@Transactional
public class DiagramDbServiceHandler implements DiagramDbService.Iface {

    private DiagramDao diagramDao;

    public DiagramDbServiceHandler(ApplicationContext context) {
        diagramDao = (DiagramDao) context.getBean("diagramDao");
        assert diagramDao != null;
    }

    @Override
    public long saveDiagram(TDiagram diagram) throws TIdAlreadyDefined {
        if (diagram.isSetId()) {
            throw new TIdAlreadyDefined("Diagram id not null. To save diagram you should not assign id to diagram.");
        }
        return diagramDao.saveDiagram(new Diagram(diagram), diagram.getFolderId());
    }

    @Override
    public TDiagram openDiagram(long diagramId) throws TNotFound {
        Diagram diagram = diagramDao.openDiagram(diagramId);
        if (diagram == null) {
            throw new TNotFound(String.valueOf(diagramId), "Diagram not found.");
        }
        return diagram.toTDiagram();
    }

    @Override
    public void deleteDiagram(long diagramId) throws TNotFound {
        if (!diagramDao.isExistsDiagram(diagramId)) {
            throw new TNotFound(String.valueOf(diagramId), "Diagram to delete not found.");
        }
        diagramDao.deleteDiagram(diagramId);
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) throws TNotFound, TIdNotDefined {
        if (!diagram.isSetId()) {
            throw new TIdNotDefined("Diagram id is null. To rewrite diagram you should specify id.");
        }
        if (!diagramDao.isExistsDiagram(diagram.getId())) {
            throw new TNotFound(String.valueOf(diagram.getId()), "Diagram to rewrite not found.");
        }
        diagramDao.rewriteDiagram(new Diagram(diagram));
    }

    @Override
    public long createFolder(TFolder folder) throws TIdAlreadyDefined {
        if (folder.isSetId()) {
            throw new TIdAlreadyDefined("Folder id not null. To save folder you should not assign id to folder.");
        }
        return diagramDao.createFolder(new Folder(folder));
    }

    @Override
    public void deleteFolder(long folderId) throws TNotFound {
        if (!diagramDao.isExistsFolder(folderId)) {
            throw new TNotFound(String.valueOf(folderId), "Folder to delete not found.");
        }
        diagramDao.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree(String username) throws TNotFound {
        Folder folder = diagramDao.getFolderTree(username);
        if (folder == null) {
            throw new TNotFound(username, "FolderTree for specified user not found.");
        }
        return folder.toTFolder();
    }
}
