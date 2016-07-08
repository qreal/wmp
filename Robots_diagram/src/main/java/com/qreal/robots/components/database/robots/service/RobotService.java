package com.qreal.robots.components.database.robots.service;

import com.fasterxml.jackson.core.JsonProcessingException;

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {
    public String sendProgram(String robotName, String program) throws JsonProcessingException;

    public String register(String name, String ssid);

    public String delete(String name);
}
