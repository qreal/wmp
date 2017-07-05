package com.qreal.wmp.generator.model;

import com.qreal.wmp.thrift.gen.TPalette;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/** Keeps information about new language. */
@Data
public class Palette implements Serializable {
    private Long id;

    private String name;

    private String userName;

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

        if (name != null) {
            tPalette.setName(name);
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
