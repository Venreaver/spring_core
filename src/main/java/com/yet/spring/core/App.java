package com.yet.spring.core;

import com.yet.spring.core.beans.Client;
import com.yet.spring.core.beans.Event;
import com.yet.spring.core.beans.EventType;
import com.yet.spring.core.loggers.EventLogger;
import com.yet.spring.core.spring.AppConfig;
import com.yet.spring.core.spring.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class App {
    @Autowired
    private Client client;

    @Resource(name = "defaultLogger")
    private EventLogger defaultLogger;

    @Resource(name = "loggerMap")
    private Map<EventType, EventLogger> loggers;

    public App() {

    }

    public App(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.defaultLogger = defaultLogger;
        this.loggers = loggers;
    }

    public void logEvent(EventType eventType, Event event, String msg) {
        event.setMsg(msg.replaceAll(client.getId(), client.getFullName()));
        EventLogger logger = loggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }
        logger.logEvent(event);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class, LoggerConfig.class);
        context.scan("com.yet.spring.core");
        context.refresh();
        context.registerShutdownHook();

        App app = (App) context.getBean("app");

        Client client = context.getBean(Client.class);
        System.out.println("Client says: " + client.getGreeting());

        Event event = context.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "Some event for user 1");

        event = (Event) context.getBean("event");
        app.logEvent(EventType.ERROR, event, "Some event for user 2");

        event = (Event) context.getBean("event");
        app.logEvent(EventType.ERROR, event, "Some event for user 3");
    }
}
