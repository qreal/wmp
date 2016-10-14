package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.dashboard.database.users.client.UserService;
import com.qreal.wmp.dashboard.database.users.model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/** Thrift client side of RobotDBService.*/
@Service("robotService")
@PropertySource("classpath:client.properties")
public class RobotServiceImpl implements RobotService {

    private static final Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    private final UserService userService;

    private TTransport transport;

    private RobotDbService.Client client;

    @Value("${port.db.robot}")
    private int port;

    @Value("${path.db.robot}")
    private String url;

    @Autowired
    public RobotServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /** Creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client RobotService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new RobotDbService.Client(protocol);
    }

    @Override
    public long register(@NotNull Robot robot) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("register() was called with parameters: robot = {}", robot.getName());

        long idRobot;
        transport.open();
        try {
            idRobot = client.registerRobot(robot.toTRobot());
        } finally {
            transport.close();
        }

        logger.trace("register() successfully registered {} robot.", robot.getName());
        return idRobot;
    }

    @Override
    public void registerByUsername(@NotNull Robot robot, String username) throws AbortedException,
            ErrorConnectionException, TException {
        logger.trace("registerByUsername() was called with parameters: robot = {}, username = {}", robot.getName(),
                username);

        User user = getUser(username);
        robot.setOwner(user);

        robot = registerRobot(robot);

        updateUserWithRobot(user, robot);

        logger.trace("registerByUsername() successfully registered {} robot with an owner {}", robot.getName(),
                username);
    }

    private Robot registerRobot(Robot robot) throws TException, AbortedException, ErrorConnectionException {
        long idRobot = register(robot);
        robot.setId(idRobot);
        logger.trace("A robot with id {} was successfully registered.", robot.getId());
        return robot;
    }

    private void updateUserWithRobot(User user, Robot robot) throws TException, AbortedException,
            ErrorConnectionException  {
        user.getRobots().add(robot);
        userService.update(user);
        logger.trace("user {} was updated.", user.getUsername());
    }

    @Override
    public @NotNull Robot findById(long id) throws NotFoundException, ErrorConnectionException, AbortedException,
            TException {
        logger.trace("findById() was called with parameters: robotId = {}.", id);
        transport.open();

        TRobot tRobot;
        try {
            tRobot = client.findById(id);
        } finally {
            transport.close();
        }

        User user = getUser(tRobot.getUsername());
        return new Robot(tRobot, user);
    }

    private User getUser(String username) throws TException, ErrorConnectionException, AbortedException {
        User user;
        try {
            user = userService.findByUserName(username);
        } catch (NotFoundException notFound) {
            logger.error("Operation was called with username of not existing user.");
            throw new AbortedException("User not exist.", "operation aborted", RobotServiceImpl.class.getName());
        }
        return user;
    }

    @Override
    public boolean isRobotExists(long id) throws ErrorConnectionException, TException {
        logger.trace("isRobotExists() was called with parameters: robotId = {}", id);
        transport.open();
        try {
            return client.isRobotExists(id);
        } finally {
            transport.close();
        }
    }

    @Override
    public void delete(long id) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("delete() called with parameters: name = {}.", id);
        transport.open();
        try {
            client.deleteRobot(id);
        } finally {
            transport.close();
        }
        logger.trace("delete() successfully deleted {} robot.", id);
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
        logger.trace("update() successfully updated {} robot", tRobot.getName());
    }
}
