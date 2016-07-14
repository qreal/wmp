package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.robots.components.dashboard.socket.SocketClient;
import com.qreal.robots.components.dashboard.model.robot.Message;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.dashboard.model.robot.RobotInfo;
import com.qreal.robots.components.dashboard.thrift.gen.RobotServiceThrift;
import com.qreal.robots.components.database.robots.DAO.RobotDAO;
import com.qreal.robots.components.database.robots.service.client.RobotService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class RobotRestServletHandler implements RobotServiceThrift.Iface {

    private AbstractApplicationContext context;
    private static final ObjectMapper mapper = new ObjectMapper();

    public RobotRestServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean registerRobot(String robotName, String ssid) throws org.apache.thrift.TException {
        RobotService robotService = (RobotService) context.getBean("RobotService");
        robotService.registerByUsername(new Robot(robotName, ssid), getUserName());
        return true;
    }

    @Override
    public boolean deleteRobot(String robotName) throws org.apache.thrift.TException {
        RobotService robotService = (RobotService) context.getBean("RobotService");
        robotService.delete(robotName);
        return true;

    }

    @Override
    public String sendProgram(String robotName, String program) throws TException {
        RobotDAO robotDAO = (RobotDAO) context.getBean("RobotDAO");
        Robot robot = robotDAO.findByName(robotName);
        SocketClient socketClient = new SocketClient(MainController.HOST_NAME, MainController.PORT);
        try {
            return socketClient.sendMessage(generateSendProgramRequest(robotName, robot.getSsid(), program, getUserName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateSendProgramRequest(String robotName, String ssid, String program, String username)
            throws JsonProcessingException {
        RobotInfo robotInfo = new RobotInfo(username, robotName, ssid);
        robotInfo.setProgram(program);
        Message message = new Message("WebApp", "sendDiagram", robotInfo);
        return mapper.writeValueAsString(message);
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
