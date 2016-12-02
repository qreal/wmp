package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

public interface PaletteService {
    Long createPalette(@NotNull Palette palette) throws AbortedException, ErrorConnectionException, TException;
}
