package com.qreal.robots.components.dashboard.model.robot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.robots.parser.ModelConfig;
import com.qreal.robots.parser.ModelConfigParser;
import com.qreal.robots.parser.SystemConfig;
import com.qreal.robots.parser.SystemConfigParser;

public class RobotInfo {

    private String owner;
    private String name;
    private String ssid;
    private String modelConfig;
    private String systemConfig;
    private String program;

    public RobotInfo() {
    }

    public RobotInfo(String owner, String name, String ssid) {
        this.owner = owner;
        this.name = name;
        this.ssid = ssid;

    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(String modelConfig) {
        this.modelConfig = modelConfig;
    }

    public String getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(String systemConfig) {
        this.systemConfig = systemConfig;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @JsonIgnore
    public SystemConfig getSystemConfigObject() {
        return new SystemConfigParser().parse(systemConfig);
    }

    @JsonIgnore
    public ModelConfig getModelConfigObject() {
        return new ModelConfigParser().parse(modelConfig);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
