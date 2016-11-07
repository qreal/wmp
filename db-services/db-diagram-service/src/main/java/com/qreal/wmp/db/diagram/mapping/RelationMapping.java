package com.qreal.wmp.db.diagram.mapping;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.TFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/** Performs loading of folder's parent for folder converting.*/
public class RelationMapping {

    private static final Logger logger = LoggerFactory.getLogger(RelationMapping.class);

    private DiagramDao diagramDao;

    public RelationMapping(DiagramDao diagramDao) {
        this.diagramDao = diagramDao;
    }

    public Folder convertTFolder(TFolder tFolder) {
        Folder folder = new Folder(tFolder);
        folder = loadParents(folder);
        return folder;
    }

    /** Loads parent folders for converted from TFolder folder.*/
    private Folder loadParents(Folder folder) {
        if (folder.getId() != null) {
            folder = loadFolderSaved(folder);
        }

        if (folder.getFolderParentId() != null) {
            folder = loadFolderParent(folder);
        }

        folder.setChildrenFolders(folder.getChildrenFolders().stream().map(this::loadParents).
                collect(Collectors.toSet()));

        return folder;
    }

    private Folder loadFolderParent(Folder folder) {
        boolean contains = false;
        for (Folder presentedDir : folder.getParentFolders()) {
            if (presentedDir.getId().equals(folder.getFolderParentId())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            Folder loadedParent = null;
            try {
                loadedParent = diagramDao.getFolder(folder.getFolderParentId());
            } catch (NotFoundException e) {
                logger.error("Consistency error: folderParentId was set, but folder doesn't exist", e);
            }
            if (loadedParent != null) {
                folder.getParentFolders().add(loadedParent);
                loadedParent.getChildrenFolders().add(folder);
            }
        }

        //Here folder with many to many. No need for folderParentId
        folder.setFolderParentId(null);
        return folder;
    }

    private Folder loadFolderSaved(Folder folder) {
        Folder loadedFolder;
        try {
            loadedFolder = diagramDao.getFolder(folder.getId());
        } catch (NotFoundException e) {
            logger.error("Consistency error: id was set, but folder doesn't exist", e);
            return folder;
        }

        for (Folder parentDir : loadedFolder.getParentFolders()) {
            boolean contains = false;
            for (Folder presentedDir : folder.getParentFolders()) {
                if (presentedDir.getId().equals(parentDir.getId())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                folder.getParentFolders().add(parentDir);
            }
        }
        return folder;
    }
}
