package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.db.diagram.model.FolderConverter;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/** Thrift server side handler for DiagramDBService.*/
public class DiagramDbServiceHandler implements DiagramDbService.Iface {
    private DiagramDao diagramDao;

    private FolderConverter converter;

    public DiagramDbServiceHandler(ApplicationContext context) {
        diagramDao = (DiagramDao) context.getBean("diagramDao");
        converter = (FolderConverter) context.getBean("folderConverter");
        assert diagramDao != null;
    }

    @Override
    public long saveDiagram(TDiagram diagram) throws TIdAlreadyDefined, TAborted {
        if (diagram.isSetId()) {
            throw new TIdAlreadyDefined("Diagram Id not null. To save a diagram you should not assign Id to it.");
        }
        long id;
        try {
            id = diagramDao.saveDiagram(new Diagram(diagram), diagram.getFolderId());
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return id;
    }

    @Override
    public TDiagram getDiagram(long diagramId) throws TNotFound {
        Diagram diagram;
        try {
            diagram = diagramDao.getDiagram(diagramId);
        }
        catch (NotFoundException e) {
            throw new TNotFound(String.valueOf(diagramId), "Diagram not found.");
        }
        return diagram.toTDiagram();
    }

    @Override
    public void deleteDiagram(long diagramId) throws TAborted {
        try {
            diagramDao.deleteDiagram(diagramId);
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public void updateDiagram(TDiagram diagram) throws TAborted, TIdNotDefined {
        if (!diagram.isSetId()) {
            throw new TIdNotDefined("Diagram id is null. To rewrite diagram you should specify id.");
        }
        try {
            diagramDao.rewriteDiagram(new Diagram(diagram));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public long saveFolder(TFolder tFolder) throws TAborted, TIdAlreadyDefined {
        long id;
        if (tFolder.isSetId()) {
            throw new TIdAlreadyDefined("Folder Id not null. To save a folder you should not assign Id to it.");
        }
        try {
            id = diagramDao.saveFolder(converter.toFolder(tFolder));
        } catch (AbortedException e) {
            //For now never happens
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return id;
    }

    @Override
    public TFolder getFolder(long folderId) throws TNotFound {
        Folder folder;
        try {
            folder = diagramDao.getFolder(folderId);
        } catch (NotFoundException e) {
            throw new TNotFound(String.valueOf(folderId), "Folder not found.");
        }
        return folder.toTFolder();
    }

    @Override
    public void updateFolder(TFolder tFolder) throws TAborted, TIdNotDefined {
        if (!tFolder.isSetId()) {
            throw new TIdNotDefined("Folder id is null. To update folder you should specify id.");
        }
        try {
            diagramDao.updateFolder(converter.toFolder(tFolder));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }



    @Override
    public void deleteFolder(long folderId) throws TAborted {
        try {
            diagramDao.deleteFolder(folderId);
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
    }

    @Override
    public TFolder getFolderTree(String username) throws TNotFound {
        Folder folder;
        try {
            folder = diagramDao.getFolderTree(username);
        } catch (NotFoundException e) {
            throw new TNotFound(username, "FolderTree for specified user not found.");
        }
        return folder.toTFolder();
    }
}
