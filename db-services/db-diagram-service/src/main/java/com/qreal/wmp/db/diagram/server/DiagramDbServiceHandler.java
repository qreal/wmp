package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.mapping.RelationMapping;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/** Thrift server side handler for DiagramDBService.*/
public class DiagramDbServiceHandler implements DiagramDbService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(DiagramDbServiceHandler.class);

    private DiagramDao diagramDao;

    private RelationMapping mapper;

    public DiagramDbServiceHandler(ApplicationContext context) {
        diagramDao = (DiagramDao) context.getBean("diagramDao");
        mapper = new RelationMapping(diagramDao);
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
            id = diagramDao.saveFolder(mapper.convertTFolder(tFolder));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return id;
    }

    @Override
    public TFolder getFolder(long folderId, String username) throws TNotFound {
        Folder folder;
        try {
            folder = diagramDao.getFolder(folderId);
        } catch (NotFoundException e) {
            throw new TNotFound(String.valueOf(folderId), "Folder not found.");
        }
        return folder.toTFolder(username);
    }

    @Override
    public void updateFolder(TFolder tFolder) throws TAborted, TIdNotDefined {
        if (!tFolder.isSetId()) {
            throw new TIdNotDefined("Folder id is null. To update folder you should specify id.");
        }
        try {
            diagramDao.updateFolder(mapper.convertTFolder(tFolder));
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
        return folder.toTFolder(username);
    }

    @Override
    public void shareFolderTo(String username, TFolder tFolderToShare) throws TException {
        Folder rootFolder = mapper.convertTFolder(getFolderTree(username));

        Folder folderToShare = mapper.convertTFolder(tFolderToShare);
        folderToShare = addOwner(username, folderToShare);
        updateFolder(folderToShare.toTFolder(username));

        if (rootFolder == null) {
            return;
        }

        Folder sharedFolder = getSharedFolder(rootFolder);

        if (sharedFolder == null) {

            sharedFolder = new Folder("Shared", username);
            long idShared = saveFolder(sharedFolder.toTFolder(username));
            sharedFolder.setId(idShared);

            sharedFolder.getChildrenFolders().add(folderToShare);
            folderToShare.getParentFolders().add(sharedFolder);

            updateFolder(sharedFolder.toTFolder(username));

            rootFolder.getChildrenFolders().add(sharedFolder);
            sharedFolder.getParentFolders().add(rootFolder);
            updateFolder(rootFolder.toTFolder(username));
        }
        else {
            if (sharedFolder.getChildrenFolders().contains(folderToShare)) {
                logger.error("addUserToOwners method was called with folder which already shared to user.");
                return;
            }
            sharedFolder.getChildrenFolders().add(folderToShare);
            updateFolder(sharedFolder.toTFolder(username));
        }
    }

    private Folder addOwner(String username, Folder folder) {
        Set<String> owners = folder.getOwners();
        owners.add(username);
        folder.setOwners(owners);
        return folder;
    }

    @Nullable
    private Folder getSharedFolder(Folder rootFolder) {
        Folder sharedFolder = null;
        for (Folder folderCur : rootFolder.getChildrenFolders()) {
            if (folderCur.getFolderName().equals("Shared")) {
                sharedFolder = folderCur;
            }
        }
        return sharedFolder;
    }
}
