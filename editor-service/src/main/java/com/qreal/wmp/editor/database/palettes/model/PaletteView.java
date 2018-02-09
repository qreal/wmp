package com.qreal.wmp.editor.database.palettes.model;

import com.qreal.wmp.thrift.gen.TPaletteView;
import lombok.Data;

/*View of palette. */
@Data
public class PaletteView {

    private Long id;

    private String name;

    public PaletteView() {
    }

    /** Constructor-converter from Thrift TPaletteView to PaletteView.*/
    public PaletteView(TPaletteView tPalette) {
        if (tPalette.isSetId()) {
            id = tPalette.getId();
        }

        if (tPalette.isSetName()) {
            name = tPalette.getName();
        }
    }

    /** Converter from PaletteView to Thrift TPaletteView.*/
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
