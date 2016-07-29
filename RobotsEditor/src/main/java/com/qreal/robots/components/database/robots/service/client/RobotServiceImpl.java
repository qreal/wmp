package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.users.service.client.UserService;
import com.qreal.robots.thrift.gen.RobotDbService;
import com.qreal.robots.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Thrift client side of RobotDBService.
 */
@Service("robotService")
public class RobotServiceImpl implements RobotService {

    private static final Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    @Autowired
    private UserService userService;

    private TTransport transport;

    private RobotDbService.Client client;

    /**
     * Constructor creates connection with Thrift TServer.
     */
    public RobotServiceImpl() {
        String url = "localhost";
        int port = 9091;
        logger.info("Client RobotService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public long register(Robot robot) {
        logger.trace("register method called with parameters: robot = {}", robot.getName());
        long idRobot = -1;
        try {
            transport.open();
            idRobot = client.registerRobot(robot.toTRobot());
            transport.close();
            logger.trace("register method registered robot {}", robot.getName());
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending register request with parameters: " +
                    "robot = {}", robot.getName(), e);
        }
        return idRobot;
    }

    @Override
    public void registerByUsername(Robot robot, String username) {
        logger.trace("registerByUsername method called with parameters: robot = {}, username = {}", robot.getName(),
                username);
        User user = userService.findByUserName(username);
        robot.setOwner(user);
        logger.trace("registering robot {}", robot.getName());
        long idRobot = register(robot);
        robot.setId(idRobot);
        logger.trace("robot registered with id {}", robot.getId());
        logger.trace("updating user {}", username);
        user.getRobots().add(robot);
        userService.update(user);
        logger.trace("user {} updated", username);
        logger.trace("registerByUsername method registered robot {} with owner {}", robot.getName(), username);
    }

    @Override
    public Robot findById(long id) {
        logger.trace("findById method called with parameters: robotId = {}", id);
        TRobot tRobot = new TRobot();
        try {
            transport.open();
            tRobot = client.findById(id);
            transport.close();
            logger.trace("findById method got result");
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending findById request with parameters: " +
                    "name = {}", id, e);
        }
        return new Robot(tRobot, userService.findByUserName(tRobot.getUsername()));
    }

    @Override
    public boolean isRobotExists(long id) {
        logger.trace("isRobotExists method called with parameters: robotId = {}", id);
        boolean isRobotExists = false;
        try {
            transport.open();
            isRobotExists = client.isRobotExists(id);
            transport.close();
            logger.trace("isRobotExists method got result");
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending isRobotExists request with " +
                    "parameters: name = {}", id, e);
        }
        return isRobotExists;
    }

    @Override
    public void delete(long id) {
        logger.trace("delete method called with parameters: name = {}", id);
        try {
            transport.open();
            client.deleteRobot(id);
            transport.close();
            logger.trace("delete method deleted robot {}", id);
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending delete request with parameters: " +
                    "name = {}", id, e);
        }
    }

    @Override
    public void update(TRobot tRobot) {
        logger.trace("update method called with parameters: tRobot = {}", tRobot.getName());
        try {
            transport.open();
            client.updateRobot(tRobot);
            transport.close();
            logger.trace("update method updated robot {}", tRobot.getName());
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending delete request with parameters: " +
                    "tRobot = {}", tRobot.getName(), e);
        }
    }
}
