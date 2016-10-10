package com.qreal.wmp.db.user.client.robots;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.RobotDbService;
import com.qreal.wmp.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/** Thrift client side of RobotDBService.*/
@Service("robotService")
@PropertySource("classpath:client.properties")
public class RobotServiceImpl implements RobotService {

    private static final Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    private TTransport transport;

    private RobotDbService.Client client;

    @Value("${port.db.robot}")
    private int port;

    @Value("${path.db.robot}")
    private String url;

    /** Connects to a Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client RobotService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public long register(@NotNull TRobot robot) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("register() was called with parameters: robot = {}.", robot.getName());
        long idRobot = -1;
        transport.open();
        try {
            idRobot = client.registerRobot(robot);
        } finally {
            transport.close();
        }
        logger.trace("register() succesfully registered robot {}", robot.getName());
        return idRobot;
    }

    @Override
    public TRobot findById(long id) throws NotFoundException, ErrorConnectionException, TException {
        logger.trace("findById() called with parameters: robotId = {}.", id);
        TRobot tRobot = null;
        transport.open();
        try {
            tRobot = client.findById(id);
        } finally {
            transport.close();
        }
        logger.trace("findById() got result successfully.");
        return tRobot;
    }

    @Override
    public boolean isRobotExists(long id) throws ErrorConnectionException, TException {
        logger.trace("isRobotExists() was called with parameters: robotId = {}.", id);
        boolean isRobotExists = false;
        transport.open();
        try {
            isRobotExists = client.isRobotExists(id);
        } finally {
            transport.close();
        }
        logger.trace("isRobotExists() got result successfully.");
        return isRobotExists;
    }

    @Override
    public void delete(long id) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("delete() was called with parameters: id = {}", id);
        transport.open();
        try {
            client.deleteRobot(id);
        } finally {
            transport.close();
        }
        logger.trace("delete() successfully deleted robot {}", id);
    }

    @Override
    public void update(@NotNull TRobot tRobot) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("update() was called with parameters: tRobot = {}.", tRobot.getName());
        transport.open();
        try {
            client.updateRobot(tRobot);
        } finally {
            transport.close();
        }
        logger.trace("update() successfully updated robot {}", tRobot.getName());

    }
}
