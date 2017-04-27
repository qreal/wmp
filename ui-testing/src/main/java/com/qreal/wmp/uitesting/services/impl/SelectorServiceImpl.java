package com.qreal.wmp.uitesting.services.impl;

import com.google.gson.JsonElement;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.utils.ConfigsMerger;
import org.jetbrains.annotations.Contract;

public class SelectorServiceImpl implements SelectorService {

    private final JsonElement config;
    
    public SelectorServiceImpl(JsonElement config) {
        this.config = config;
    }
    
    @Override
    public String get(String element, Attribute attr) {
        return find(element).getAsJsonObject().get(attr.toString()).getAsString();
    }
    
    @Override
    public String get(Attribute attr) {
        return config.getAsJsonObject().get(attr.toString()).getAsString();
    }
    
    @Override
    public SelectorService create(String sublevel) {
        return new SelectorServiceImpl(find(sublevel));
    }
    
    @Contract("_ -> !null")
    public static SelectorService getFirstSelectorService(ConfigsMerger merger) {
        return new SelectorServiceImpl(merger.generateCommonConfig());
    }
    
    private JsonElement find(String element) {
        String[] parts = element.split("\\.");
        JsonElement tmp = config;
        for (int i = 0; i < parts.length - 1; ++i) {
            tmp = tmp.getAsJsonObject().get(parts[i]);
        }
        return parts.length > 0
                ? tmp.getAsJsonObject().get(parts[parts.length - 1])
                : tmp.getAsJsonObject().get(element);
    }
}
