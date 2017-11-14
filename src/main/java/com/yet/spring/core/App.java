package com.yet.spring.core;

import com.yet.spring.core.aspects.StatisticsAspect;
import com.yet.spring.core.beans.Client;
import com.yet.spring.core.beans.Event;
import com.yet.spring.core.beans.EventType;
import com.yet.spring.core.loggers.EventLogger;
import com.yet.spring.core.spring.AppConfig;
import com.yet.spring.core.spring.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class App {
    @Autowired
    private Client client;

    @Autowired
    private StatisticsAspect statisticsAspect;

    @Value("#{T(com.yet.spring.core.beans.Event).isDay(8, 17) ? cacheFileEventLogger : consoleEventLogger}")
    private EventLogger defaultLogger;

    @Value("#{'Hello user ' + (systemProperties['os.arch'].equals('amd64') " +
            "? systemEnvironment['USERNAME'] : systemEnvironment['USER']) " +
            "+'. Default logger is ' + app.defaultLogger.name}")
    private String startupMessage;

    @Resource(name = "loggerMap")
    private Map<EventType, EventLogger> loggers;

    public App() {

    }

    public App(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.defaultLogger = defaultLogger;
        this.loggers = loggers;
    }

    public EventLogger getDefaultLogger() {
        return defaultLogger;
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
        System.out.println(app.startupMessage);
        Client client = context.getBean(Client.class);
        System.out.println("Client says: " + client.getGreeting());

        Event event = context.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "Some event for user 1");

        event = context.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "One more event for 1");

        event = context.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "And one more event for 1");

        event = (Event) context.getBean("event");
        app.logEvent(EventType.ERROR, event, "Some event for user 2");

        event = (Event) context.getBean("event");
        app.logEvent(EventType.ERROR, event, "Some event for user 3");

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
