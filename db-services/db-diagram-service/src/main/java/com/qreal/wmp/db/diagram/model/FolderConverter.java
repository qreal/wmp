package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service("folderConverter")
public class FolderConverter {

    private static final Logger logger = LoggerFactory.getLogger(FolderConverter.class);

    private DiagramDao diagramDao;

    @Autowired
    public FolderConverter(DiagramDao diagramDao) {
        this.diagramDao = diagramDao;
    }

    /** Constructor-converter from Thrift TFolder to Folder.*/
    public Folder toFolder(TFolder tFolder) {

        Folder folder = new Folder();

        if (tFolder.isSetId()) {
            folder.setId(tFolder.getId());
        }

        if (tFolder.isSetFolderName()) {
            folder.setFolderName(tFolder.getFolderName());
        }

        if (tFolder.isSetOwners()) {
            folder.setOwners(tFolder.getOwners());
        }

        if (tFolder.isSetChildrenFolders()) {
            folder.setChildrenFolders(tFolder.getChildrenFolders().stream().map(this::toFolder).
                    collect(Collectors.toSet()));

            for (Folder folderCur : folder.getChildrenFolders()) {
                folderCur.getParentFolders().add(folder);
            }
        }

        if (tFolder.isSetParentFolders()) {
            for (long folderId : tFolder.getParentFolders()) {
                try {
                    Folder parentFolder = diagramDao.getFolder(folderId);
                    folder.getParentFolders().add(parentFolder);
                    parentFolder.getChildrenFolders().add(folder);
                } catch (NotFoundException e) {
                    logger.error("Inconsistent state: Passed parent which not exists", e);
                }
            }
        }

        if (tFolder.isSetDiagrams()) {
            folder.setDiagrams(tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toSet()));
        }
        return folder;
    }

}
