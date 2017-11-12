package com.yet.spring.core.spring;

import com.yet.spring.core.beans.EventType;
import com.yet.spring.core.loggers.*;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

@Configuration
public class LoggerConfig {
    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() {
        return new PropertyPlaceholderConfigurer();
    }

    @Resource(name = "consoleEventLogger")
    private ConsoleEventLogger consoleEventLogger;
    @Resource(name = "fileEventLogger")
    private FileEventLogger fileEventLogger;
    @Resource(name = "cacheFileEventLogger")
    private CacheFileEventLogger cacheFileEventLogger;
    @Resource(name = "combinedEventLogger")
    private CombinedEventLogger combinedEventLogger;

    @Bean
    public Collection<EventLogger> combinedLoggers() {
        Collection<EventLogger> loggers = new ArrayList<>(2);
        loggers.add(consoleEventLogger);
        loggers.add(fileEventLogger);
        return loggers;
    }

    @Bean
    public Map<EventType, EventLogger> loggerMap() {
        Map<EventType, EventLogger> map = new EnumMap<EventType, EventLogger>(EventType.class);
        map.put(EventType.INFO, consoleEventLogger);
        map.put(EventType.ERROR, combinedEventLogger);
        return map;
    }

    @Bean()
    public EventLogger defaultLogger() {
        return cacheFileEventLogger;
    }
}
