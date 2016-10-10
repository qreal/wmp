package com.qreal.wmp.db.user.exceptions.translation;

import com.qreal.wmp.db.user.client.diagrams.DiagramServiceImpl;
import com.qreal.wmp.db.user.client.robots.RobotServiceImpl;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.transport.TTransportException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Profile("default")
public class ExceptionTranslation {

    private static final Logger loggerDiagram = LoggerFactory.getLogger(DiagramServiceImpl.class);

    private static final Logger loggerRobot = LoggerFactory.getLogger(RobotServiceImpl.class);


    @Around("execution(* com.qreal.wmp.db.user.client.diagrams.*.*(..))")
    public Object catchExceptionDiagram(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TIdNotDefined e) {
            loggerDiagram.error("Client DiagramService encountered an IdNotDefined exception.", e);
        } catch (TAborted e) {
            throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
        } catch (TErrorConnection e) {
            throw new ErrorConnectionException(e.getClientName(), e.getMessage());
        } catch (TNotFound e) {
            throw new NotFoundException(e.getId(), e.getMessage());
        } catch (TTransportException e) {
            loggerDiagram.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                    "a problem  while opening transport.");
        }
        return null;
    }

    @Around("execution(* com.qreal.wmp.db.user.client.robots.*.*(..))")
    public Object catchExceptionRobots(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TIdNotDefined e) {
            loggerRobot.error("Client RobotService encountered an IdNotDefined exception.", e);
        } catch (TIdAlreadyDefined e) {
            loggerRobot.error("Client RobotService encountered an IdAlreadyDefined exception.", e);
        } catch (TAborted e) {
            throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
        } catch (TErrorConnection e) {
            throw new ErrorConnectionException(e.getClientName(), e.getMessage());
        } catch (TNotFound e) {
            throw new NotFoundException(e.getId(), e.getMessage());
        } catch (TTransportException e) {
            loggerRobot.error("Client RobotService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(RobotServiceImpl.class.getName(), "Client RobotService encountered " +
                    "a problem  while opening transport.");
        }
        return null;
    }
}
