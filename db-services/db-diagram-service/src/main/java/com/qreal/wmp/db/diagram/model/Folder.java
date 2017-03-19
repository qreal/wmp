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

    private static final String ROOT_FOLDER = "root";
    
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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "childrenFolders")
    private Set<Folder> parentFolders = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
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

        childrenFolders = childrenFolders.stream().map(x -> {
            x.getParentFolders().add(this);
            return x;
        }).collect(Collectors.toSet());

        if (tFolder.isSetDiagrams()) {
            diagrams = tFolder.getDiagrams().stream().map(Diagram::new).collect(Collectors.toSet());
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    
    public void remove() {
        parentFolders.forEach(x -> x.removeChild(id));
        parentFolders.clear();
    }
    
    public Set<Folder> getChildrenFolders() {
        return childrenFolders;
    }
    
    public Set<String> getOwners() {
        return owners;
    }
    
    public void setOwners(Set<String> owners) {
        this.owners = owners;
    }
    
    public Long getFolderParentId() {
        return folderParentId;
    }
    
    public void setFolderParentId(Long folderParentId) {
        this.folderParentId = folderParentId;
    }
    
    public Set<Folder> getParentFolders() {
        return parentFolders;
    }
    
    public void setParentFolders(Set<Folder> parentFolders) {
        this.parentFolders = parentFolders;
    }
    
    public void setChildrenFolders(Set<Folder> childrenFolders) {
        this.childrenFolders = childrenFolders;
    }
    
    public Set<Diagram> getDiagrams() {
        return diagrams;
    }
    
    public void setDiagrams(Set<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
    
    public String getFolderName() {
        return folderName;
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

        setFolderParentId(username, tFolder);

        if (childrenFolders != null && !childrenFolders.isEmpty()) {
            tFolder.setChildrenFolders(childrenFolders.stream().map(folder -> folder.toTFolder(username)).
                    collect(Collectors.toSet()));
        }

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;
    }

    private void setFolderParentId(String username, TFolder tFolder) {
        parentFolders.stream().filter(dir -> dir.owners.contains(username)).
                forEach(dir -> tFolder.setFolderParentId(dir.getId()));

        if (!tFolder.isSetFolderParentId() && folderParentId != null) {
            tFolder.setFolderParentId(folderParentId);
        }
    }
    
    private void removeChild(long childId) {
        childrenFolders.stream().filter(x -> x.id == childId).findFirst().ifPresent(x -> childrenFolders.remove(x));
    }
}

