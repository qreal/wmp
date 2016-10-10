package com.qreal.wmp.db.robot.exceptions.translation;

import com.qreal.wmp.db.robot.client.users.UserServiceImpl;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.TAborted;
import com.qreal.wmp.thrift.gen.TErrorConnection;
import com.qreal.wmp.thrift.gen.TIdNotDefined;
import com.qreal.wmp.thrift.gen.TNotFound;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Around("execution(* com.qreal.wmp.db.robot.client.users.*.*(..))")
    public Object catchException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TIdNotDefined e) {
            logger.error("Client UserService encountered an IdNotDefined exception.", e);
        } catch (TAborted e) {
            throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
        } catch (TErrorConnection e) {
            throw new ErrorConnectionException(e.getClientName(), e.getMessage());
        } catch (TNotFound e) {
            throw new NotFoundException(e.getId(), e.getMessage());
        } catch (TTransportException e) {
            logger.error("Client UserService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "a problem  while opening transport.");
        }
        return null;
    }
}
