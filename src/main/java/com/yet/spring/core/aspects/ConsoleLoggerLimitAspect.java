package com.yet.spring.core.aspects;

import com.yet.spring.core.beans.Event;
import com.yet.spring.core.loggers.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;

public class ConsoleLoggerLimitAspect {
    private final int maxCount;
    private final EventLogger otherLogger;
    private int currentCount = 0;

    public ConsoleLoggerLimitAspect(int maxCount, EventLogger otherLogger) {
        this.maxCount = maxCount;
        this.otherLogger = otherLogger;
    }

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