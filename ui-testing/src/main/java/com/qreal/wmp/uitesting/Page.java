package com.qreal.wmp.uitesting;

public enum Page {
    Auth("auth"), Dashboard("dashboard"), EditorRobots("robotsEditor"), EditorBPMN("bpmnEditor");
    
    private String identify;
    
    Page(String identify) {
        this.identify = identify;
    }
    
    public String getIdentify() {
        return identify;
    }
}
