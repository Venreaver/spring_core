package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;

import java.util.Collection;

public class CombinedEventLogger implements EventLogger {
    Collection<EventLogger> loggers;

    public CombinedEventLogger(Collection<EventLogger> loggers) {
        this.loggers = loggers;
    }

    @Override
    public void logEvent(Event event) {
        loggers.forEach(lg -> lg.logEvent(event));
    }
}
