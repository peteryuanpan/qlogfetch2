package com.peter.execute;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class ExecuteDebug {

    public static void setLoggerLevel(String logger, Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(logger).setLevel(level);
    }

    public static void setAllLoggerLevelDebug() {
        setLoggerLevel("com.peter", Level.DEBUG);
    }
}
