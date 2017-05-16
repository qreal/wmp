package com.qreal.wmp.uitesting.mousegestures;

import java.util.List;

/** Describes gestures for JSON parsing. */
public class Gesture {
    
    private String name;
    
    private List<String> key;
    
    private double factor;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getKey() {
        return key;
    }
    
    public void setKey(List<String> key) {
        this.key = key;
    }
    
    public double getFactor() {
        return factor;
    }
    
    public void setFactor(double factor) {
        this.factor = factor;
    }
}
