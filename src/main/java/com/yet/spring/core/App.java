package com.yet.spring.core;

import com.yet.spring.core.aspects.StatisticsAspect;
import com.yet.spring.core.beans.Client;
import com.yet.spring.core.beans.Event;
import com.yet.spring.core.beans.EventType;
import com.yet.spring.core.loggers.EventLogger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class App {
    private Client client;
    private EventLogger defaultLogger;
    private Map<EventType, EventLogger> loggers;
    private String startupMessage;
    private StatisticsAspect statisticsAspect;

    public App(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.defaultLogger = defaultLogger;
        this.loggers = loggers;
    }

    public void setStartupMessage(String startupMessage) {
        this.startupMessage = startupMessage;
    }

    public void setStatisticsAspect(StatisticsAspect statisticsAspect) {
        this.statisticsAspect = statisticsAspect;
    }

    public EventLogger getDefaultLogger() {
        return defaultLogger;
    }

    private void logEventMsg(EventType eventType, Event event, String msg) {
        event.setMsg(msg.replaceAll(client.getId(), client.getFullName()));
        EventLogger logger = loggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }
        logger.logEvent(event);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx =
                new ClassPathXmlApplicationContext("spring.xml", "loggers.xml", "aspects.xml", "db.xml");
        ctx.registerShutdownHook();
        App app = (App) ctx.getBean("app");
        System.out.println(app.startupMessage);
        Client client = ctx.getBean(Client.class);
        System.out.println("Client says: " + client.getGreeting());
        Event event = ctx.getBean(Event.class);
        app.logEventMsg(EventType.INFO, event, "Some event for user 1");
        event = ctx.getBean(Event.class);
        app.logEventMsg(EventType.INFO, event, "One more event for 1");
        event = ctx.getBean(Event.class);
        app.logEventMsg(EventType.INFO, event, "And one more event for 1");
        event = (Event) ctx.getBean("event");
        app.logEventMsg(EventType.ERROR, event, "Some event for user 2");
        app.outPutLoggingCounter();
    }

    private void outPutLoggingCounter() {
        if (statisticsAspect != null) {
            System.out.println("Loggers statistics. Number of calls: ");
            statisticsAspect.getCounter()
                    .forEach((key, value) -> System.out.println("   " + key.getSimpleName() + ": " + value));
        }
    }
}
