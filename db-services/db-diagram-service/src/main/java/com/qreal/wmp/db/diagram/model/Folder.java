package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Folder with diagrams and other folders. */
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
    @ElementCollection
    private Set<String> owners = new HashSet<>();

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

    public Folder(String folderName, String owners) {
        this.folderName = folderName;
        this.owners.add(owners);
    }

    /** Constructor-converter from Thrift TFolder to Folder.*/
    public Folder(TFolder tFolder) {

        if (tFolder.isSetId()) {
            id = tFolder.getId();
        }

        if (tFolder.isSetFolderName()) {
            folderName = tFolder.getFolderName();
        }

        if (tFolder.isSetOwners()) {
            owners = tFolder.getOwners();
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

        if (owners != null) {
            tFolder.setOwners(owners);
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
}
