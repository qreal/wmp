package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.thrift.gen.RobotDbService;
import com.qreal.robots.thrift.gen.TRobot;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("robotService")
public class RobotServiceImpl implements RobotService {

    private static final Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    @Autowired
    private UserService userService;

    private TTransport transport;

    private RobotDbService.Client client;

    public RobotServiceImpl() {
        String url = "localhost";
        int port = 9091;
        logger.info("Client RobotService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public void register(Robot robot) {
        logger.trace("register method called with parameters: robot = {}", robot.getName());

        try {
            transport.open();
            client.registerRobot(robot.toTRobot());
            transport.close();
            logger.trace("register method registered robot {}", robot.getName());
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending register request with parameters: " +
                    "robot = {}", robot.getName(), e);
        }
    }

    @Override
    public void registerByUsername(Robot robot, String username) {
        logger.trace("registerByUsername method called with parameters: robot = {}, username = {}", robot.getName(),
                username);
        robot.setOwner(userService.findByUserName(username));
        register(robot);
        logger.trace("registerByUsername method registered robot {} with owner {}", robot.getName(), username);
    }

    @Override
    public Robot findByName(String name) {
        logger.trace("findByName method called with parameters: robotName = {}", name);
        TRobot tRobot = new TRobot();
        try {
            transport.open();
            tRobot = client.findByName(name);
            transport.close();
            logger.trace("findByName method got result");
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending findByName request with parameters: " +
                    "name = {}", name, e);
        }
        return new Robot(tRobot, userService.findByUserName(tRobot.getUsername()));
    }

    @Override
    public void delete(String name) {
        logger.trace("delete method called with parameters: name = {}", name);
        try {
            transport.open();
            client.deleteRobot(name);
            transport.close();
            logger.trace("delete method deleted robot {}", name);
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending delete request with parameters: " +
                    "name = {}", name, e);
        }
    }
}