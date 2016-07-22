package com.qreal.robots.components.database.robots.service.client;

import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.robots.service.server.RobotDbServiceHandler;
import com.qreal.robots.components.database.robots.thrift.gen.RobotDbService;
import com.qreal.robots.components.database.robots.thrift.gen.TRobot;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("RobotService")
public class RobotServiceImpl implements RobotService {

    @Autowired
    private UserService userService;

    private TTransport transport;

    private RobotDbService.Client client;

    public RobotServiceImpl() {
        transport = new TSocket("localhost", 9091);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public void register(Robot robot) throws TException {
        transport.open();
        client.registerRobot(RobotDbServiceHandler.convertFromRobot(robot));
        transport.close();
    }

    @Override
    public void registerByUsername(Robot robot, String username) throws TException {
        robot.setOwner(userService.findByUserName(username));
        register(robot);
    }

    @Override
    public Robot findByName(String name) throws TException {
        transport.open();
        TRobot tRobot = client.findByName(name);
        transport.close();
        return RobotDbServiceHandler.convertToRobot(tRobot, userService.findByUserName(tRobot.getUsername()));
    }

    @Override
    public void delete(String name) throws TException {
        transport.open();
        client.deleteRobot(name);
        transport.close();
    }
}