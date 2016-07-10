package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qreal.robots.components.dashboard.thrift.gen.RobotServiceThrift;
import com.qreal.robots.components.database.robots.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

class RobotRestServletHandler implements RobotServiceThrift.Iface {

    private AbstractApplicationContext context;

    public RobotRestServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean registerRobot(String robotName, String ssid) throws org.apache.thrift.TException {
        RobotService robotService = (RobotService) context.getBean("RobotService");
        robotService.register(robotName, ssid);
        return true;
    }

    @Override
    public boolean deleteRobot(String robotName) throws org.apache.thrift.TException {
        RobotService robotService = (RobotService) context.getBean("RobotService");
        robotService.delete(robotName);
        return true;

    }

    @Override
    public boolean sendProgram(String robotName, String program) throws org.apache.thrift.TException {
        RobotService robotService = (RobotService) context.getBean("RobotService");
        try {
            robotService.sendProgram(robotName, program);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
