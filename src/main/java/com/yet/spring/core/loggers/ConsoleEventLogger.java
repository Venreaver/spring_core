package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;

public class ConsoleEventLogger extends AbstractLogger {
    public void logEvent(Event event) {
        System.out.print(event);
    }
}
