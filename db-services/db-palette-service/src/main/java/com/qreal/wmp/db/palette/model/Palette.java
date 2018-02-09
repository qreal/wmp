package com.qreal.wmp.db.palette.model;

import com.qreal.wmp.thrift.gen.TPalette;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "palettes")
@Data
public class Palette implements Serializable {
    @Id
    @Column(name = "palette_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name")
    private String paletteName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "palette_id", referencedColumnName = "palette_id")
    private Set<Node> nodes = new HashSet<>();

    @Column(name = "username")
    private String userName;

    public Palette() {
    }

    public Palette(String paletteName, String userName) {
        this.paletteName = paletteName;
        this.userName = userName;
    }

    /** Constructor-converter from Thrift TPalette to Palette.*/
    public Palette(TPalette tPalette) {
        if (tPalette.isSetId()) {
            id = tPalette.getId();
        }

        if (tPalette.isSetName()) {
            paletteName = tPalette.getName();
        }

        if (tPalette.isSetUser()) {
            userName = tPalette.getUser();
        }

        if (tPalette.isSetNodes()) {
            nodes = tPalette.getNodes().stream().map(Node::new).collect(Collectors.toSet());
        }
    }

    /** Converter from Palette to Thrift TPalette.*/
    public TPalette toTPalette() {
        TPalette tPalette = new TPalette();

        if (id != null) {
            tPalette.setId(id);
        }

        if (paletteName != null) {
            tPalette.setName(paletteName);
        }

        if (userName != null) {
            tPalette.setUser(userName);
        }

        if (nodes != null && !nodes.isEmpty()) {
            tPalette.setNodes(nodes.stream().map(Node::toTNode).
                    collect(Collectors.toSet()));
        }

        return tPalette;
    }
}
