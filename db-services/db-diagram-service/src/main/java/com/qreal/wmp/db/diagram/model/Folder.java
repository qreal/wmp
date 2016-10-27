package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Column(name = "username")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> owners = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "childrenFolders")
    private Set<Folder> parentFolders = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="folders_folders", joinColumns={@JoinColumn(name="parent_id")},
            inverseJoinColumns={@JoinColumn(name="child_id")})
    private Set<Folder> childrenFolders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id")
    private Set<Diagram> diagrams = new HashSet<>();

    public Folder() { }

    public Folder(String folderName, String owners) {
        this.folderName = folderName;
        this.owners.add(owners);
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

        if (owners != null && !owners.isEmpty()) {
            tFolder.setOwners(owners);
        }

        if (childrenFolders != null && !childrenFolders.isEmpty()) {
            tFolder.setChildrenFolders(childrenFolders.stream().map(Folder::toTFolderWithoutParents).collect(Collectors.toSet()));
        }

        if (parentFolders != null && !parentFolders.isEmpty()) {
            tFolder.setParentFolders(parentFolders.stream().map(Folder::getId).collect(Collectors.toSet()));
        }

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;

    }

    /** Converter from Folder to Thrift TFolder.*/
    public TFolder toTFolderWithoutParents() {
        TFolder tFolder = new TFolder();

        if (id != null) {
            tFolder.setId(id);
        }

        if (folderName != null) {
            tFolder.setFolderName(folderName);
        }

        if (owners != null && !owners.isEmpty()) {
            tFolder.setOwners(owners);
        }

        if (childrenFolders != null && !childrenFolders.isEmpty()) {
            tFolder.setChildrenFolders(childrenFolders.stream().map(Folder::toTFolderWithoutParents).collect(Collectors.toSet()));
        }

        //not pass parents for now

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;

    }
}
