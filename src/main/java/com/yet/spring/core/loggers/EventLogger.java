package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;

public interface EventLogger {
    void logEvent(Event event);

    public String getName();
}
