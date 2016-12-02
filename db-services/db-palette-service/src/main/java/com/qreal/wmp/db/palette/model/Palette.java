package com.qreal.wmp.db.palette.model;

import com.qreal.wmp.thrift.gen.TPalette;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

/** Palette (now only graphs). */
@Entity
@Table(name = "diagrams")
@Data
public class Palette implements Serializable {
    @Id
    @Column(name = "palette_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "palette_id", referencedColumnName = "palette_id")
    private Set<Node> nodes = new HashSet<>();

    public Palette() {
    }

    /** Constructor-converter from Thrift TPalette to Palette.*/
    public Palette(TPalette tPalette) {
        if (tPalette.isSetId()) {
            id = tPalette.getId();
        }

        if (tPalette.isSetName()) {
            name = tPalette.getName();
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

        if (name != null) {
            tPalette.setName(name);
        }

        if (nodes != null && !nodes.isEmpty()) {
            tPalette.setNodes(nodes.stream().map(Node::toTNode).
                    collect(Collectors.toSet()));
        }

        return tPalette;
    }
}
