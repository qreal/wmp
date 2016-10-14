package com.qreal.wmp.editor.database.diagrams.model;

import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Folder with diagrams and other folders.*/
@Data
public class Folder implements Serializable {
    private Long id;

    private String folderName;

    private String userName;

    private Long folderParentId;

    private List<Folder> childrenFolders = new ArrayList<>();

    private List<Diagram> diagrams = new ArrayList<>();

    public Folder() { }

    public Folder(String folderName, String userName) {
        this.folderName = folderName;
        this.userName = userName;
    }

    /** Constructor-converter from Thrift TFolder to Folder.*/
    public Folder(TFolder tFolder) {

        if (tFolder.isSetId()) {
            id = tFolder.getId();
        }

        if (tFolder.isSetFolderName()) {
            folderName = tFolder.getFolderName();
        }

        if (tFolder.isSetUserName()) {
            userName = tFolder.getUserName();
        }

        if (tFolder.isSetFolderParentId()) {
            folderParentId = tFolder.getFolderParentId();
        }

        if (tFolder.isSetChildrenFolders()) {
            childrenFolders = tFolder.getChildrenFolders().stream().map(Folder::new).collect(Collectors.toList());
        }

        if (tFolder.isSetDiagrams()) {
            diagrams = tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toList());
        }

    }

    /** Converter from Folder to Thrift TFolder.*/
    public TFolder toTFolder() {
        TFolder tFolder = new TFolder();

        if (id != null) {
            tFolder.setId(id);
        }

        if (folderName != null) {
            tFolder.setFolderName(folderName);
        }

        if (userName != null) {
            tFolder.setUserName(userName);
        }

        if (folderParentId != null) {
            tFolder.setFolderParentId(folderParentId);
        }

        if (childrenFolders != null) {
            tFolder.setChildrenFolders(childrenFolders.stream().map(Folder::toTFolder).collect(Collectors.toSet()));
        }

        if (diagrams != null) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;
    }
}
