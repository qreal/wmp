/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.robots.controller.MainController;
import com.qreal.robots.dao.RobotDAO;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.robot.Message;
import com.qreal.robots.model.robot.Robot;
import com.qreal.robots.model.robot.RobotInfo;
import com.qreal.robots.model.robot.RobotWrapper;
import com.qreal.robots.parser.ModelConfig;
import com.qreal.robots.parser.ModelConfigValidator;
import com.qreal.robots.parser.SystemConfig;
import com.qreal.robots.parser.ValidationResult;
import com.qreal.robots.service.UserService;
import com.qreal.robots.socket.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ageevdenis on 02-3-15.
 */

@RestController
public class RobotRestService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private RobotDAO robotDAO;

    @ResponseBody
    @RequestMapping(value = "/sendDiagram", method = RequestMethod.POST)
    public String sendProgram(@RequestParam("robotName") String robotName, @RequestParam("program") String program)
            throws JsonProcessingException {
        Robot robot = robotDAO.findByName(robotName);
        SocketClient socketClient = new SocketClient(MainController.HOST_NAME, MainController.PORT);
        return socketClient.sendMessage(generateSendProgramRequest(robotName, robot.getSsid(), program));
    }

    @ResponseBody
    @RequestMapping(value = "/registerRobot", method = RequestMethod.POST)
    public String register(@RequestParam("robotName") String name, @RequestParam("ssid") String ssid) {
        User user = userService.findByUserName(getUserName());
        if (!userRobotExists(user, name)) {
            robotDAO.save(new Robot(name, ssid, user));
            return "{\"status\":\"OK\"}";
        } else {
            return String.format("{\"status\":\"ERROR\", \"message\":\"Robot with name %s is already exists\"}", name);
        }
    }

    private boolean userRobotExists(User user, String name) {
        for (Robot robot : user.getRobots()) {
            if (robot.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteRobot", method = RequestMethod.POST)
    public String delete(@RequestParam("robotName") String name) {
        Robot robot = robotDAO.findByName(name);
        robotDAO.delete(robot);
        return "{\"message\":\"OK\"}";
    }

    @ResponseBody
    @RequestMapping(value = "/saveModelConfig", method = RequestMethod.POST)
    public String saveModelConfig(HttpSession session, @RequestParam("robotName") String robotName, @RequestParam("modelConfigJson") String modelConfigJson,
                                  @RequestParam("typeProperties") String typeProperties) throws IOException {
        ModelConfig modelConfig = getModelConfig(modelConfigJson, typeProperties);
        Robot robot = robotDAO.findByName(robotName);

        List<RobotWrapper> fullRobotInfo = (List<RobotWrapper>) session.getAttribute("fullRobotInfo");

        SystemConfig systemConfig = getSystemConfig(robot, fullRobotInfo);
        assert systemConfig != null;

        ModelConfigValidator validator = new ModelConfigValidator(systemConfig);
        ValidationResult result = validator.validate(modelConfig);
        if (!result.hasErrors()) {
            SocketClient socketClient = new SocketClient(MainController.HOST_NAME, MainController.PORT);
            return socketClient.sendMessage(generateSendModelConfigRequest(robotName, robot.getSsid(), modelConfig));
        } else {
            return buildResultMessage(result);
        }
    }

    private SystemConfig getSystemConfig(Robot robot, List<RobotWrapper> fullRobotInfo) {
        for (RobotWrapper robotWrapper : fullRobotInfo) {
            if (robotWrapper.getRobot().getName().equals(robot.getName())) {
                return robotWrapper.getRobotInfo().getSystemConfigObject();
            }
        }
        return null;
    }

    private String buildResultMessage(ValidationResult result) {
        return String.format("{\"status\":\"%s\", \"errors\":\"%s\"}", result.isOk(), result.getErrorsString());
    }

    private ModelConfig getModelConfig(String modelConfigJson, String typeProperties) throws IOException {
        List<Map<String, String>> mapList = mapper.readValue(modelConfigJson,
                new TypeReference<List<HashMap<String, String>>>() {
                });
        List<Map<String, String>> propertyList = mapper.readValue(typeProperties,
                new TypeReference<List<HashMap<String, String>>>() {
                });

        Map<String, String> map = new HashMap<>();
        for (Map lMap : mapList) {
            map.putAll(lMap);
        }

        return new ModelConfig(map, propertyList);
    }

    private String generateSendProgramRequest(String robotName, String ssid, String program)
            throws JsonProcessingException {
        RobotInfo robotInfo = new RobotInfo(getUserName(), robotName, ssid);
        robotInfo.setProgram(program);
        Message message = new Message("WebApp", "sendDiagram", robotInfo);
        return mapper.writeValueAsString(message);
    }

    private String generateSendModelConfigRequest(String robotName, String ssid, ModelConfig modelConfig)
            throws JsonProcessingException {
        RobotInfo robotInfo = new RobotInfo(getUserName(), robotName, ssid);
        robotInfo.setModelConfig(modelConfig.convertToXml());
        Message message = new Message("WebApp", "sendModelConfig", robotInfo);
        return mapper.writeValueAsString(message);

    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
