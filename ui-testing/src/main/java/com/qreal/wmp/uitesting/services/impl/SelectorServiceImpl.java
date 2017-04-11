package com.qreal.wmp.uitesting.services.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.utils.ConfigsMerger;
import org.jetbrains.annotations.Contract;
import org.springframework.core.env.Environment;

import java.util.Map;

public class SelectorServiceImpl implements SelectorService {

    private final JsonElement config;
    
    public SelectorServiceImpl(JsonElement config) {
        this.config = config;
    }
    
    @Override
    public String getId(String element) {
        return config.getAsJsonObject().get(element).getAsJsonObject().get("id").getAsString();
    }
    
    @Override
    public String getText(String element) {
        return config.getAsJsonObject().get(element).getAsJsonObject().get("text").getAsString();
    }
    
    @Override
    public String getSelector(String element) {
        JsonElement asked = config.getAsJsonObject().get(element);
        return asked.isJsonPrimitive() ? asked.getAsString() : asked.getAsJsonObject().get("selector").getAsString();
    }
    
    @Override
    public String getName(String element) {
        return config.getAsJsonObject().get(element).getAsJsonObject().get("name").getAsString();
    }
    
    @Override
    public String getType(String element) {
        return config.getAsJsonObject().get(element).getAsJsonObject().get("type").getAsString();
    }
    
    @Override
    public SelectorService create(String sublevel) {
        String[] parts = sublevel.split(".");
        JsonElement tmp = config;
        for (int i = 0; i < parts.length - 1; ++i) {
            tmp = tmp.getAsJsonObject().get(parts[i]);
        }
        return sublevel.contains(".")
                ? new SelectorServiceImpl(tmp.getAsJsonObject().get(parts[parts.length - 1]))
                : new SelectorServiceImpl(tmp.getAsJsonObject().get(sublevel));
    }
    
    @Contract("_ -> !null")
    public static SelectorService getFirstSelectorService(ConfigsMerger merger) {
        return new SelectorServiceImpl(merger.generateCommonConfig());
    }
}
