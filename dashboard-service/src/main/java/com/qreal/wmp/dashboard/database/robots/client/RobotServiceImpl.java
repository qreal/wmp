package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.dashboard.database.users.client.UserService;
import com.qreal.wmp.dashboard.database.users.model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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
    public long register(@NotNull Robot robot) throws AbortedException, ErrorConnectionException {
        logger.trace("register() was called with parameters: robot = {}", robot.getName());
        // Usage of atomics here is to be able to modify this value in lambda function. AtomicLong serves
        // as immutable wrapper to mutable value on a heap, just like "ref" in F#.
        final AtomicLong idRobot = new AtomicLong(-1);
        ThriftRequest request = new ThriftRequest(transport, "register", "robot = " + robot.getName() + ".")
                .registerHandler(
                    TIdAlreadyDefined.class,
                    e -> logger.error("register() encountered IdAlreadyDefined exception. Robot was not"
                        + " registered.", e))
                .registerHandler(
                    /// TODO: Is this handler really needed? Default handler does the same, but also logs exception.
                    TErrorConnection.class,
                    (TErrorConnection e) -> {
                        throw new ErrorConnectionException(e.getClientName(), e.getMessage());
                    });

        try {
            request.run(() -> idRobot.set(client.registerRobot(robot.toTRobot())));
        } catch (ThriftRequest.RethrownException e) {
            if (e.getCause() instanceof ErrorConnectionException) {
                throw (ErrorConnectionException) e.getCause();
            } else {
                throw new IncorrectRethrownException(e.getCause());
            }
        }

        logger.trace("register() successfully registered {} robot.", robot.getName());
        return idRobot.get();
    }

    @Override
    public void registerByUsername(@NotNull Robot robot, String username) throws AbortedException,
            ErrorConnectionException {
        logger.trace("registerByUsername() was called with parameters: robot = {}, username = {}", robot.getName(),
                username);
        User user;
        try {
            user = userService.findByUserName(username);
        } catch (NotFoundException notFound) {
            logger.error("registerByUsername() was called with username of not existing user.");
            return;
        }

        robot.setOwner(user);

        long idRobot = register(robot);
        robot.setId(idRobot);
        logger.trace("A robot with id {} was successfully registered.", robot.getId());

        user.getRobots().add(robot);
        userService.update(user);
        logger.trace("user {} was updated.", username);
        logger.trace("registerByUsername() successfully registered {} robot with an owner {}", robot.getName(), username);
    }

    @Override
    public @NotNull Robot findById(long id) throws NotFoundException, ErrorConnectionException {
        logger.trace("findById() was called with parameters: robotId = {}.", id);
        // Here we use array with one element as an immutable wrapper for a mutable value. Java really needs ref cells.
        final TRobot[] tRobot = new TRobot[1];
        tRobot[0] = new TRobot();
        ThriftRequest request = new ThriftRequest(transport, "findById", "name = " + id + ".")
                .registerHandler(
                    TNotFound.class,
                    (TNotFound e) -> { throw new NotFoundException(e.getId(), e.getMessage()); });

        try {
            request.run(() -> tRobot[0] = client.findById(id));
        } catch (AbortedException e) {
            throw new RuntimeException("Operation that should not be aborted was aborted", e);
        } catch (ThriftRequest.RethrownException e) {
            if (e.getCause() instanceof NotFoundException) {
                throw (NotFoundException) e.getCause();
            } else {
                throw new IncorrectRethrownException(e.getCause());
            }
        }

        logger.trace("findById() got result.");
        User user = null;
        try {
            user = userService.findByUserName(tRobot[0].getUsername());
        } catch (NotFoundException e) {
            logger.error("Inconsistent state: Robot contains user with id {}, but this user doesn't exist.", e);
        }

        return new Robot(tRobot[0], user);
    }

    @Override
    public boolean isRobotExists(long id) throws ErrorConnectionException {
        logger.trace("isRobotExists() was called with parameters: robotId = {}", id);
        final AtomicBoolean isRobotExists = new AtomicBoolean(false);
        ThriftRequest request = new ThriftRequest(transport, "isRobotExists", "name = " + id + ".");
        try {
            request.run(() -> isRobotExists.set(client.isRobotExists(id)));
        } catch (AbortedException e) {
            throw new InvalidAbortException(e);
        }

        logger.trace("isRobotExists method got result");
        return isRobotExists.get();
    }

    @Override
    public void delete(long id) throws AbortedException, ErrorConnectionException {
        logger.trace("delete() called with parameters: name = {}.", id);
        ThriftRequest request = new ThriftRequest(transport, "delete", "name = " + id + ".");
        request.run(() -> client.deleteRobot(id));
        logger.trace("delete() successfully deleted {} robot.", id);
    }

    @Override
    public void update(@NotNull TRobot tRobot) throws AbortedException, ErrorConnectionException {
        logger.trace("update() was called with parameters: tRobot = {}.", tRobot.getName());
        ThriftRequest request = new ThriftRequest(transport, "update", "tRobot = " + tRobot.getName() + ".")
                .registerHandler(TIdNotDefined.class,
                        e -> logger.error("update() encountered an IdNotDefined exception. You've tried to update"
                                + " a robot, but did not specified its id.", e));
        request.run(() -> client.updateRobot(tRobot));
        logger.trace("update() successfully updated {} robot", tRobot.getName());
    }

    /** Thrown when exception was thrown by exception handler but was not expected in a method. */
    public static class IncorrectRethrownException extends RuntimeException {
        IncorrectRethrownException(Throwable e) {
            super("Unknown exception was rethrown by handler", e);
        }
    }

    /** Thrown when operation that can not be aborted was aborted. */
    public static class InvalidAbortException extends RuntimeException {
        InvalidAbortException(Throwable e) {
            super("Operation that should not be aborted was aborted", e);
        }
    }

    /**
     * Class that implements actual Thrift transport call and handles all exceptions that may occur.
     * Allows to register handler for each exception, also allows to throw new exceptions in handlers (it wraps them
     * into ThriftRequest.RethrownException unchecked exceptions since Java type system does not allow to parameterize
     * "throws" clause). Also provides handler for general TException exception which wraps it into
     * ErrorConnectionException. TAborted exception is wrapped into AbortedException.
     */
    private static class ThriftRequest {
        /** Thrift transport object. */
        private @NotNull TTransport transport;

        /**
         * A list of registered exception handlers. Any exception that occurs during request is passed through this
         * list until some handler processes it.
         */
        private @NotNull List<Handler<Exception>> exceptionHandlers = new LinkedList<>();

        /** Handler for TException, called last. */
        private @NotNull Handler<TException> defaultHandler;

        /**
         * Constructor.
         * @param transport Thrift transport object.
         * @param methodName name of a called method, for logging purposes.
         * @param parameters string representing parameters of the called method, for logging purposes.
         */
        ThriftRequest(@NotNull TTransport transport, @NotNull String methodName, @NotNull String parameters) {
            this.transport = transport;
            defaultHandler =
                    (TException e) -> {
                        logger.error("Client RobotService encountered a problem while sending {} request with"
                                + " parameters: {}", methodName, parameters, e);
                        throw new ErrorConnectionException(
                                RobotServiceImpl.class.getName(),
                                "Client RobotService encountered a problem while sending " + methodName + " request");
                    };
        }

        /**
         * Registers handler for an exception.
         * @param exceptionClass class of an exception to register.
         * @param handler function that handles this exception.
         * @return reference to this object, to allow method chaining.
         */
        <T extends Exception> ThriftRequest registerHandler(Class<T> exceptionClass, HandlingAction<T> handler) {
            exceptionHandlers.add(e -> {
                if (e.getClass().equals(exceptionClass)) {
                    @SuppressWarnings("unchecked")
                    T ex = (T)e;
                    handler.handle(ex);
                    return true;
                }

                return false;
            });

            return this;
        }

        /**
         * Executes request by opening transport and running function passed to this method.
         * @param action function that actually executes request and can throw an exception in the process.
         * @throws AbortedException when request is aborted.
         * @throws ErrorConnectionException when there is a connection error.
         */
        void run(@NotNull ActionThatMayFail action) throws AbortedException, ErrorConnectionException {
            try {
                transport.open();
                try {
                    action.run();
                } catch (TAborted e) {
                    throw new AbortedException(
                            e.getTextCause(),
                            e.getMessage(),
                            e.getFullClassName());
                } catch (Exception e) {
                    try {
                        for (Handler<Exception> handler : exceptionHandlers) {
                            if (handler.handle(e)) {
                                return;
                            }
                        }

                        if (e instanceof TException) {
                            defaultHandler.handle((TException) e);
                        }
                    } catch (AbortedException | ErrorConnectionException ex) {
                        throw ex;
                    } catch (Exception rethrownException) {
                        throw new RethrownException(rethrownException);
                    }

                    throw new UnhandledExceptionInThriftException(e);
                } finally {
                    transport.close();
                }
            } catch (TTransportException e) {
                logger.error("Client RobotService encountered a problem while opening transport.", e);
                throw new ErrorConnectionException(RobotServiceImpl.class.getName(), "Client RobotService encountered"
                        + " a problem while opening transport.");
            }
        }

        /**
         * Thrown when exception was not handled by any registered handler and by default handler. Means that something
         * unexpected happen.
         */
        public static class UnhandledExceptionInThriftException extends RuntimeException {
            UnhandledExceptionInThriftException(Exception e) {
                super(e);
            }
        }

        /**
         * Wrapper for an exception that was rethrown in handler. It is unchecked to avoid declaring it in "throws"
         * clause, so methods using such exceptions shall remember to catch them themselves.
         */
        static class RethrownException extends RuntimeException {
            RethrownException(Exception e) {
                super(e);
            }
        }

        /** Interface for lambda actions that may throw an exception. */
        private interface ActionThatMayFail {
            void run() throws Exception;
        }

        /** Interface for exception handling lambdas which can tell if they handled an exception. */
        private interface Handler<T extends Exception> {
            boolean handle(T e) throws Exception;
        }

        /** Interface for "user-side" exception handling lambdas. */
        private interface HandlingAction<T extends Exception> {
            void handle(T e) throws Exception;
        }
    }
}
