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

public class TestCacheFileEventLogger {
    private File file;

    @Before
    public void createFile() throws IOException {
        this.file = File.createTempFile("test", "CacheFileEventLogger");
    }

    @After
    public void removeFile() {
        file.delete();
    }

    private CacheFileEventLogger createAndInitCacheFileEventLogger()
            throws IOException {
        CacheFileEventLogger logger = new CacheFileEventLogger(file.getAbsolutePath(), 2);
        logger.init();
        logger.initCache();
        return logger;
    }

    @Test
    public void testLogEvent() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        CacheFileEventLogger logger = createAndInitCacheFileEventLogger();
        String contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertTrue("File is empty initially", contents.isEmpty());
        logger.logEvent(event);
        contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertTrue("File is empty as events in cache", contents.isEmpty());
        logger.logEvent(event);
        contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertFalse("File not empty, cache was dumped", contents.isEmpty());
    }

    @Test
    public void testDestroy() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        CacheFileEventLogger logger = createAndInitCacheFileEventLogger();
        String contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertTrue("File is empty initially", contents.isEmpty());
        logger.logEvent(event);
        contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertTrue("File is empty as events in cache", contents.isEmpty());
        logger.destroy();
        contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Assert.assertFalse("File not empty, cache was dumped", contents.isEmpty());
    }
}
