package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TFolder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "folders")
public class Folder implements Serializable {

    @Id
    @Column(name = "folder_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "username")
    private String userName;

    @Column(name = "folder_parent_id")
    private Long folderParentId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_parent_id", insertable = false, updatable = false)
    private List<Folder> childrenFolders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id")
    private List<Diagram> diagrams;

    public Folder() {
    }

    public Folder(String folderName, String userName) {
        this.folderName = folderName;
        this.userName = userName;
    }

    public Folder(TFolder tFolder) {

        if (tFolder.isSetFolderId()) {
            this.setFolderId(tFolder.getFolderId());
        }

        if (tFolder.isSetFolderName()) {
            this.setFolderName(tFolder.getFolderName());
        }

        if (tFolder.isSetUserName()) {
            this.setUserName(tFolder.getUserName());
        }

        if (tFolder.isSetFolderParentId()) {
            this.setFolderParentId(tFolder.getFolderParentId());
        }

        if (tFolder.isSetChildrenFolders()) {
            childrenFolders = tFolder.getChildrenFolders().stream().map(Folder::new).collect(Collectors.toList());
        }

        if (tFolder.isSetDiagrams()) {
            diagrams = tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toList());
        }

    }

    public TFolder toTFolder() {
        TFolder tFolder = new TFolder();

        if (this.folderId != null) {
            tFolder.setFolderId(this.folderId);
        }

        if (this.folderName != null) {
            tFolder.setFolderName(this.folderName);
        }

        if (this.userName != null) {
            tFolder.setUserName(this.userName);
        }

        if (this.folderParentId != null) {
            tFolder.setFolderParentId(this.folderParentId);
        }

        if (this.childrenFolders != null) {
            tFolder.setChildrenFolders(this.childrenFolders.stream().map(Folder::toTFolder).collect(Collectors.toSet()));
        }

        if (this.diagrams != null) {
            tFolder.setDiagrams(this.diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderName(String name) {
        this.folderName = name;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderParentId(Long folderParentId) { this.folderParentId = folderParentId; }

    public Long getFolderParentId() {
        return this.folderParentId;
    }

    public void setChildrenFolders(List<Folder> folderParentId) {
        this.childrenFolders = folderParentId;
    }

    public List<Folder> getChildrenFolders() {
        return this.childrenFolders;
    }

    public void setDiagrams(List<Diagram> diagrams) {
        this.diagrams = diagrams;
    }

    public List<Diagram> getDiagrams() {
        return this.diagrams;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }
}
