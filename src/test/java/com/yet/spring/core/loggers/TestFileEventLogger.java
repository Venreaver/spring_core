package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;

public class TestFileEventLogger {
    private File file;

    @Before
    public void createFile() throws IOException {
        this.file = File.createTempFile("test", "FileEventLogger");
    }

    @After
    public void removeFile() {
        file.delete();
    }

    @Test
    public void testInit() throws IOException {
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        logger.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitFail() throws IOException {
        file.setReadOnly();
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        logger.init();
    }

    @Test
    public void testLogEvent() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        logger.init();
        String contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertTrue(contents.isEmpty());
        logger.logEvent(event);
        contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertFalse(contents.isEmpty());
    }
}
