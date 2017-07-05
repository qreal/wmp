package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.palettes.model.Model;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

public interface AcceleoService {
    /**
     * Creates Metamodel.
     *
     * @param palette  palette to be created
     */
    void createMetamodel(@NotNull Palette palette) throws AbortedException, ErrorConnectionException, TException;

    /**
     * Creates Model and generate code.
     *
     * @param model  diagram to be created
     */
    void generate(@NotNull Model model) throws AbortedException, ErrorConnectionException, TException;
}
