package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Folder with diagrams and other folders. */
@Entity
@Table(name = "folders")
@Data
@EqualsAndHashCode(exclude = "parentFolders")
@ToString(exclude = "parentFolders")
public class Folder implements Serializable {

    @Id
    @Column(name = "folder_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_name")
    private String folderName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "owners", joinColumns = @JoinColumn(name = "folder_id"))
    @Column(name = "username")
    private Set<String> owners = new HashSet<>();

    @Transient
    private Long folderParentId;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "childrenFolders")
    private Set<Folder> parentFolders = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "folders_folders", joinColumns = {@JoinColumn(name = "parent_id")},
            inverseJoinColumns = {@JoinColumn(name = "child_id")})
    private Set<Folder> childrenFolders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id")
    private Set<Diagram> diagrams = new HashSet<>();

    public Folder() { }

    public Folder(String folderName, String owners, Long id) {
        this.folderName = folderName;
        this.owners.add(owners);
        this.id = id;
    }

    public Folder(String folderName, String owners) {
        this.folderName = folderName;
        this.owners.add(owners);
    }

    /**
     * Constructor-converter from Thrift TFolder to Folder.
     * It will not fill parentsFolders except presented, which could be changed.
     */
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

        for (Object child : childrenFolders.toArray()) {
            childrenFolders.remove((Folder) child);
            ((Folder) child).getParentFolders().add(this);
            childrenFolders.add((Folder) child);
        }

        if (tFolder.isSetDiagrams()) {
            diagrams = tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toSet());
        }
    }

    /** Converter from Folder to Thrift TFolder.*/
    public TFolder toTFolder(final String username) {
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

        parentFolders.stream().filter(dir -> dir.owners.contains(username)).
                forEach(dir -> tFolder.setFolderParentId(dir.getId()));

        if (!tFolder.isSetFolderParentId() && folderParentId != null) {
            tFolder.setFolderParentId(folderParentId);
        }

        if (childrenFolders != null && !childrenFolders.isEmpty()) {
            tFolder.setChildrenFolders(childrenFolders.stream().map((folder -> folder.toTFolder(username))).
                    collect(Collectors.toSet()));
        }

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;
    }
}

