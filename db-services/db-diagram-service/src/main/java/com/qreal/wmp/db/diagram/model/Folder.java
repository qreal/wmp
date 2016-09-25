package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Folder with diagrams and other folders.*/
@Entity
@Table(name = "folders")
@Data
public class Folder implements Serializable {

    @Id
    @Column(name = "folder_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "username")
    private String userName;

    //FIXME
    //Do we really need this field?
    @Column(name = "folder_parent_id")
    private Long folderParentId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_parent_id", insertable = false, updatable = false)
    private Set<Folder> childrenFolders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id")
    private Set<Diagram> diagrams = new HashSet<>();

    public Folder() {
    }

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
            childrenFolders = tFolder.getChildrenFolders().stream().map(Folder::new).collect(Collectors.toSet());
        }

        if (tFolder.isSetDiagrams()) {
            diagrams = tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toSet());
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

        if (childrenFolders != null && !childrenFolders.isEmpty()) {
            tFolder.setChildrenFolders(childrenFolders.stream().map(Folder::toTFolder).collect(Collectors.toSet()));
        }

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setFolderName(String name) {
        this.folderName = name;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderParentId(Long folderParentId) {
        this.folderParentId = folderParentId;
    }

    public Long getFolderParentId() {
        return this.folderParentId;
    }

    public void setChildrenFolders(Set<Folder> folderParentId) {
        this.childrenFolders = folderParentId;
    }

    @NotNull
    public Set<Folder> getChildrenFolders() {
        return this.childrenFolders;
    }

    public void setDiagrams(Set<Diagram> diagrams) {
        this.diagrams = diagrams;
    }

    @NotNull
    public Set<Diagram> getDiagrams() {
        return this.diagrams;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }
}
