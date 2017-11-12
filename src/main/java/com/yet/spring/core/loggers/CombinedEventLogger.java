package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;

@Component
public class CombinedEventLogger implements EventLogger {
    @Resource(name = "combinedLoggers")
    Collection<EventLogger> loggers;

    @Override
    public void logEvent(Event event) {
        loggers.forEach(lg -> lg.logEvent(event));
    }
}
