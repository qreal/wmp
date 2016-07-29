package com.qreal.robots.client;

import com.qreal.robots.dao.UserDao;
import com.qreal.robots.thrift.gen.RobotDbService;
import com.qreal.robots.thrift.gen.TRobot;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
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
    public long register(TRobot robot) {
        logger.trace("register method called with parameters: robot = {}", robot.getName());
        long idRobot = -1;
        try {
            transport.open();
            idRobot = client.registerRobot(robot);
            transport.close();
            logger.trace("register method registered robot {}", robot.getName());
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending register request with parameters: " +
                    "robot = {}", robot.getName(), e);
        }
        return idRobot;
    }

    @Override
    public TRobot findById(long id) {
        logger.trace("findById method called with parameters: robotId = {}", id);
        TRobot tRobot = null;
        try {
            transport.open();
            tRobot = client.findById(id);
            transport.close();
            logger.trace("findById method got result");
        } catch (TException e) {
            logger.error("Client RobotService encountered problem while sending findById request with parameters: " +
                    "name = {}", id, e);
        }
        return tRobot;
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
            logger.error("Client RobotService encountered problem while sending isRobotExists request with parameters: " +
                    "name = {}", id, e);
        }
        return isRobotExists;
    }

    @Override
    public void delete(long id) {
        logger.trace("delete method called with parameters: id = {}", id);
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
