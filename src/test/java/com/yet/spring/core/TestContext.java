package com.yet.spring.core;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yet.spring.core.beans.Client;
import com.yet.spring.core.loggers.EventLogger;

public class TestContext {

    @Test
    public void testPropertyPlaceholderSystemOverride() {
        System.setProperty("id", "35");
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        Client client = ctx.getBean(Client.class);
        ctx.close();

        assertEquals("35", client.getId());
    }

    @Test
    public void testLoggersNames() {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

        EventLogger fileLogger = ctx.getBean("fileEventLogger", EventLogger.class);
        EventLogger cacheLogger = ctx.getBean("cacheFileEventLogger", EventLogger.class);
        EventLogger combinedLogger = ctx.getBean("combinedEventLogger", EventLogger.class);

        @SuppressWarnings("unchecked")
        List<EventLogger> combinedLoggers = ctx.getBean("combinedLoggersList", List.class);

        assertEquals(fileLogger.getName() + " with cache", cacheLogger.getName());

        Collection<String> combinedNames = combinedLoggers.stream()
                .map(EventLogger::getName).collect(Collectors.toList());

        assertEquals("Combined " + combinedNames, combinedLogger.getName());

        ctx.close();
    }

}