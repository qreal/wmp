package com.qreal.wmp.db.diagram.mapping;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.TFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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

        for (Object child : folder.getChildrenFolders().toArray()) {
            folder.getChildrenFolders().remove((Folder) child);
            child = loadParents((Folder) child);
            folder.getChildrenFolders().add((Folder) child);
        }

        return folder;
    }

    private Folder loadFolderParent(Folder folder) {
        boolean contains = false;
        for (Folder presentedDir : folder.getParentFolders()) {
            if (Objects.equals(presentedDir.getId(), folder.getFolderParentId())) {
                contains = true;
            }
        }
        if (!contains) {
            Folder loadedParent = null;
            try {
                loadedParent = diagramDao.getFolder(folder.getFolderParentId());
            } catch (NotFoundException e) {
                logger.error("Consistency error: id was set, but folder doesn't exists", e);
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
        Folder loadedFolder = null;
        try {
            loadedFolder = diagramDao.getFolder(folder.getId());
        } catch (NotFoundException e) {
            //logger.error("Consistency violation", e);
        }

        if (loadedFolder != null) {
            for (Folder parentDir : loadedFolder.getParentFolders()) {
                boolean contains = false;
                for (Folder presentedDir : folder.getParentFolders()) {
                    if (Objects.equals(presentedDir.getId(), parentDir.getId())) {
                        contains = true;
                    }
                }
                if (!contains) {
                    folder.getParentFolders().add(parentDir);
                }
            }
        }
        return folder;
    }
}
