package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileEventLogger implements EventLogger {
    private File file;
    private String filename;

    FileEventLogger(String filename) {
        this.filename = filename;
    }

    void init() throws IOException{
        this.file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("Can't write into " + filename);
        }
    }

    public void logEvent(Event event) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(event.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
