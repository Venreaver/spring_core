package com.yet.spring.core.aspects;

import com.yet.spring.core.beans.Event;
import com.yet.spring.core.loggers.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConsoleLoggerLimitAspect {
    private final int maxCount;
    private final EventLogger otherLogger;
    private int currentCount = 0;

    public ConsoleLoggerLimitAspect(@Value("2") int maxCount,
                                    @Qualifier("fileEventLogger") EventLogger otherLogger) {
        this.maxCount = maxCount;
        this.otherLogger = otherLogger;
    }

    @Around("execution(* *.logEvent(com.yet.spring.core.beans.Event)) " +
            "&& within(com.yet.spring.core.loggers.ConsoleEventLogger) " +
            "&& args(event)")
    public void aroundLogEvent(ProceedingJoinPoint jp, Event event) throws Throwable {
        if (currentCount < maxCount) {
            System.out.println("ConsoleEventLogger max count is not reached. Continue...");
            ++currentCount;
            jp.proceed(new Object[]{event});
        } else {
            System.out.println("ConsoleEventLogger max count is reached. Logging to " + otherLogger.getName());
            otherLogger.logEvent(event);
        }
    }
}
