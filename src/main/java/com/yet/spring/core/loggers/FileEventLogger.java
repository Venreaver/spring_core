package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileEventLogger implements EventLogger {
    private File file;
    private String filename;

    public FileEventLogger(String filename) {
        this.filename = filename;
    }

    void init() {
        this.file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("Can't create a file with " + filename);
            }
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("Can't write into " + filename);
        }
    }

    public void logEvent(Event event) {
        try (FileWriter fw = new FileWriter(file, true)) {
//            fw.write(1);
            fw.write(event.toString());
        } catch (IOException e) {
            System.err.println("Can't create a file or write to a file " + filename);
        }
    }
}
