package com.qreal.wmp.db.palette.model;

import com.qreal.wmp.thrift.gen.TPaletteView;
import lombok.Data;

/* View of palette for client side.*/
@Data
public class PaletteView
{
    private Long id;

    private String name;

    public PaletteView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /** Constructor-converter from Thrift TPalette to Palette.*/
    public PaletteView(TPaletteView tPalette) {
        if (tPalette.isSetId()) {
            id = tPalette.getId();
        }

        if (tPalette.isSetName()) {
            name = tPalette.getName();
        }
    }

    /** Converter from Palette to Thrift TPalette.*/
    public TPaletteView toTPalette() {
        TPaletteView tPalette = new TPaletteView();

        if (id != null) {
            tPalette.setId(id);
        }

        if (name != null) {
            tPalette.setName(name);
        }

        return tPalette;
    }
}
