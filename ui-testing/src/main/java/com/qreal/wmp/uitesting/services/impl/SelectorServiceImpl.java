package com.qreal.wmp.uitesting.services.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jetbrains.annotations.Contract;

public class SelectorServiceImpl implements SelectorService {

    private final JsonElement config;
    
    public SelectorServiceImpl(JsonElement config) {
        this.config = config;
    }
    
    @Override
    public String getId() {
        return config.getAsJsonObject().get("id").getAsString();
    }
    
    @Override
    public String getId(String element) {
        return find(element).getAsJsonObject().get("id").getAsString();
    }
    
    @Override
    public String getSelector() {
        return config.getAsJsonObject().get("selector").getAsString();
    }
    
    @Override
    public String getSelector(String element) {
        return find(element).getAsJsonObject().get("selector").getAsString();
    }
    
    @Override
    public SelectorService create(String sublevel) {
        return new SelectorServiceImpl(find(sublevel));
    }
    
    @Contract("_ -> !null")
    public static SelectorService getFirstSelectorService(String jsonString) {
        return new SelectorServiceImpl(new JsonParser().parse(jsonString));
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
