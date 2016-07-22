package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.components.editor.thrift.gen.TDiagram;
import com.qreal.robots.components.editor.thrift.gen.TFolder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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
        if (tFolder.isSetFolderParentId()) {
            this.setFolderParentId(tFolder.getFolderParentId());
        }
        List<Diagram> diagrams = new ArrayList<>();
        if (tFolder.getDiagrams() != null) {
            Iterator<TDiagram> itrD = tFolder.getDiagrams().iterator();
            while (itrD.hasNext()) {
                TDiagram diagram = itrD.next();
                diagrams.add(new Diagram(diagram));
            }
        }

        List<Folder> children = new ArrayList<Folder>();
        if (tFolder.getChildrenFolders() != null) {
            Iterator<TFolder> itrF = tFolder.getChildrenFolders().iterator();
            while (itrF.hasNext()) {
                TFolder child = itrF.next();
                children.add(new Folder(child));
            }
        }

        this.setDiagrams(diagrams);
        this.setChildrenFolders(children);
    }

    public TFolder toTFolder() {
        TFolder folder = new TFolder();
        if (this.getFolderId() != null) {
            folder.setFolderId(this.getFolderId());
        }
        folder.setFolderName(this.getFolderName());
        if (this.getFolderParentId() != null) {
            folder.setFolderParentId(this.getFolderParentId());
        }

        Set<TDiagram> diagrams = new HashSet<TDiagram>();
        if (this.getDiagrams() != null) {
            Iterator<Diagram> itrD = this.getDiagrams().iterator();
            while (itrD.hasNext()) {
                Diagram diagram = itrD.next();
                diagrams.add(diagram.toTDiagram());
            }
        }
        Set<TFolder> children = new HashSet<TFolder>();
        if (this.getChildrenFolders() != null) {
            Iterator<Folder> itrF = this.getChildrenFolders().iterator();
            while (itrF.hasNext()) {
                Folder child = itrF.next();
                children.add(child.toTFolder());
            }
        }
        folder.setDiagrams(diagrams);
        folder.setChildrenFolders(children);

        return folder;
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
