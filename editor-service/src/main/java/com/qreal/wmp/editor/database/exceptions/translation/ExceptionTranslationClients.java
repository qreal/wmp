package com.qreal.wmp.editor.database.exceptions.translation;

import com.qreal.wmp.editor.database.palettes.client.PaletteServiceImpl;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.users.client.UserServiceImpl;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/** Provides translation from Thrift exception to exceptions specific for application.*/
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ExceptionTranslationClients {

    private static final Logger loggerDiagrams = LoggerFactory.getLogger(PaletteServiceImpl.class);

    private static final Logger loggerUsers = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Advice will be weaved to UserService class and will provide translation from
     * Thrift exceptions to application specific. Weaving performed by Spring at application
     * initialization step.
     */
    @Around("execution(* com.qreal.wmp.editor.database.users.client.*.*(..))")
    public Object catchExceptionUsers(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TIdNotDefined e) {
            loggerUsers.error("Client UserService encountered an IdNotDefined exception.", e);
        } catch (TAborted e) {
            throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
        } catch (TErrorConnection e) {
            throw new ErrorConnectionException(e.getClientName(), e.getMessage());
        } catch (TNotFound e) {
            throw new NotFoundException(e.getId(), e.getMessage());
        } catch (TTransportException e) {
            loggerUsers.error("Client UserService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "a problem  while opening transport.");
        } catch (TException e) {
            loggerUsers.error("Client UserService encountered a problem with connection");
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                    " a problem while sending request");
        }
        return null;
    }

    /**
     * Advice will be weaved to DiagramService class and will provide translation from
     * Thrift exceptions to application specific. Weaving performed by Spring at application
     * initialization step.
     */
    @Around("execution(* com.qreal.wmp.editor.database.diagrams.client.*.*(..))")
    public Object catchExceptionDiagrams(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TIdNotDefined e) {
            loggerDiagrams.error("Client DiagramService encountered an IdNotDefined exception.", e);
        } catch (TIdAlreadyDefined e) {
            loggerDiagrams.error("Client DiagramService encountered an IdAlreadyDefined exception.", e);
        } catch (TAborted e) {
            throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
        } catch (TErrorConnection e) {
            throw new ErrorConnectionException(e.getClientName(), e.getMessage());
        } catch (TNotFound e) {
            throw new NotFoundException(e.getId(), e.getMessage());
        } catch (TTransportException e) {
            loggerDiagrams.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client DiagramService encountered " +
                    "a problem  while opening transport.");
        } catch (TException e) {
            loggerDiagrams.error("Client DiagramService encountered a problem with connection");
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client DiagramService encountered" +
                    " a problem while sending request");
        }
        return null;
    }
}
