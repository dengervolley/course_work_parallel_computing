package com.coursework.loggers;

public interface ILogger {
    void logError(Exception e);
    void logError(String message);

    void logInfo(String message);
}
