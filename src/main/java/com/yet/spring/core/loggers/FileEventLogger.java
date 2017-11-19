package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component("fileEventLogger")
public class FileEventLogger extends AbstractLogger {
    @Value("${events.file:target/events_log.txt}")
    private String filename;
    private File file;

    public FileEventLogger() {
    }

    public FileEventLogger(String filename) {
        this.filename = filename;
    }

    @PostConstruct
    void init() throws IOException {
        this.file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("Can't write into " + filename);
        }
    }

    @Override
    public void logEvent(Event event) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(event.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("File logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
