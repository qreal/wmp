package com.qreal.wmp.editor.database.diagrams.model;

import com.qreal.wmp.editor.controller.EditorController;
import com.qreal.wmp.editor.database.diagrams.client.DiagramService;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TFolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Folder with diagrams and other folders.*/
@Data
@EqualsAndHashCode(exclude = "parentFolders")
@ToString(exclude = "parentFolders")
public class Folder implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Folder.class);

    private static ApplicationContext context;

    private Long id;

    private String folderName;

    private Set<String> owners = new HashSet<>();

    private Set<Folder> parentFolders = new HashSet<>();

    private Set<Folder> childrenFolders = new HashSet<>();

    private Set<Diagram> diagrams = new HashSet<>();

    public Folder() { }

    public Folder(String folderName, String owner) {
        this.folderName = folderName;
        this.owners.add(owner);
    }

    /** Constructor-converter from Thrift TFolder to Folder.*/
    public Folder(TFolder tFolder) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");

        if (tFolder.isSetId()) {
            id = tFolder.getId();
        }

        if (tFolder.isSetFolderName()) {
            folderName = tFolder.getFolderName();
        }

        if (tFolder.isSetOwners()) {
            owners = tFolder.getOwners();
        }

        if (tFolder.isSetParentFolders()) {
            for (long folderId : tFolder.getParentFolders()) {
                try {
                    Folder parentFolder = diagramService.getFolder(folderId);
                    parentFolders.add(parentFolder);
                    parentFolder.getChildrenFolders().add(this);
                } catch (ErrorConnectionException e) {
                    //TODO Here we should not return null, but send exception to client side.
                    logger.error("getFolder method encountered exception ErrorConnection. Instead of folder will be " +
                            "returned null.", e);
                } catch (NotFoundException e) {
                    //TODO Here we should not return 0, but send exception to client side.
                    logger.error("getFolder method encountered exception NotFound. Folder was not found. Instead of it null" +
                            "will be returned.", e);
                } catch (TException e) {
                    logger.error("TException was not translated", e);
                }
            }
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

    /** Converter from Folder to Thrift TFolder without parents.*/
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
            tFolder.setChildrenFolders(childrenFolders.stream().map(Folder::toTFolder).collect(Collectors.toSet()));
        }

        //not pass parents for now

        if (diagrams != null && !diagrams.isEmpty()) {
            tFolder.setDiagrams(diagrams.stream().map(Diagram::toTDiagram).collect(Collectors.toSet()));
        }

        return tFolder;

    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        Folder.context = context;
    }
}
