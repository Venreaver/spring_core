package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component
public class CombinedEventLogger extends AbstractLogger {
    @Resource(name = "combinedLoggers")
    private Collection<EventLogger> loggers;

    @Override
    public void logEvent(Event event) {
        loggers.forEach(lg -> lg.logEvent(event));
    }

    @Value("#{'Combined ' + combinedEventLogger.loggers.![name].toString()}")
    @Override
    protected void setName(String name) {
        this.name = name;
    }

    public Collection<EventLogger> getLoggers() {
        return loggers;
    }
}
