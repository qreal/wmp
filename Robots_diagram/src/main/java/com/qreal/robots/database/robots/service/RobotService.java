package com.qreal.robots.database.robots.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tanvd on 07.07.16.
 */

//SaveModelConfig was temporary deleted. You can find it in old repository.

public interface RobotService {
    public String sendProgram(String robotName, String program) throws JsonProcessingException;

    public String register(String name, String ssid);

    public String delete(String name);
}
