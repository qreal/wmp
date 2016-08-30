package com.qreal.wmp.db.user.client.robots;

import com.qreal.wmp.db.user.client.exceptions.NotFound;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
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

    /** Creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client RobotService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public long register(@NotNull TRobot robot) {
        logger.trace("register method called with parameters: robot = {}", robot.getName());
        long idRobot = -1;
        try {
            transport.open();
            try {
                idRobot = client.registerRobot(robot);
            } catch (TIdAlreadyDefined e) {
                logger.error("register method encountered exception IdAlreadyDefined. Robot was not registered.", e);
            } catch (TException e) {
                logger.error("Client RobotService encountered problem while sending register request with  " +
                        "parameters: robot = {}", robot.getName(), e);
            } finally {
                transport.close();
            }
        }
        catch (TTransportException e) {
            logger.error("Client RobotService encountered problem while opening transport.", e);
        }
        logger.trace("register method registered robot {}", robot.getName());
        return idRobot;
    }

    @Override
    public TRobot findById(long id) throws NotFound {
        logger.trace("findById method called with parameters: robotId = {}", id);
        TRobot tRobot = null;
        try {
            transport.open();
            try {
                tRobot = client.findById(id);
            } catch (TNotFound e) {
                throw new NotFound(e.getId(), e.getMessage());
            } catch (TException e) {
                logger.error("Client RobotService encountered problem while sending findById request with parameters:" +
                        " name = {}", id, e);
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client RobotService encountered problem while opening transport.", e);
        }
        logger.trace("findById method got result");
        return tRobot;
    }

    @Override
    public boolean isRobotExists(long id) {
        logger.trace("isRobotExists method called with parameters: robotId = {}", id);
        boolean isRobotExists = false;
        try {
            transport.open();
            try {
                isRobotExists = client.isRobotExists(id);
            } catch (TException e) {
                logger.error("Client RobotService encountered problem while sending isRobotExists request with " +
                        "parameters: name = {}", id, e);
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client RobotService encountered problem while opening transport.", e);
        }
        logger.trace("isRobotExists method got result");
        return isRobotExists;
    }

    @Override
    public void delete(long id) {
        logger.trace("delete method called with parameters: id = {}", id);
        try {
            transport.open();
            try {
                client.deleteRobot(id);
            } catch (TNotFound e) {
                logger.error("delete method encountered exception NotFound. You've tried to delete not existed robot" +
                        ".", e);
            } catch (TException e) {
                logger.error("Client RobotService encountered problem while sending delete request with parameters: " +
                        "name = {}", id, e);
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client RobotService encountered problem while opening transport.", e);
        }
        logger.trace("delete method deleted robot {}", id);
    }

    @Override
    public void update(@NotNull TRobot tRobot) {
        logger.trace("update method called with parameters: tRobot = {}", tRobot.getName());
        try {
            transport.open();
            try {
                client.updateRobot(tRobot);
            } catch (TIdNotDefined e) {
                logger.error("update method encountered exception IdNotDefined. You've tried to update robot, but not" +
                        " specified it's id.", e);
            } catch (TNotFound e) {
                logger.error("update method encountered exception NotFound. You've tried to update not existed robot" +
                        ".", e);
            } catch (TException e) {
                logger.error("Client RobotService encountered problem while sending delete request with parameters: " +
                        "tRobot = {}", tRobot.getName(), e);
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client RobotService encountered problem while opening transport.", e);
        }
        logger.trace("update method updated robot {}", tRobot.getName());

    }
}
