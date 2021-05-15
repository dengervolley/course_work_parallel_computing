package com.coursework.loggers;

public class ConsoleLogger implements ILogger{
    @Override
    public void logError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void logError(String message) {
        this.logInfo(message);
    }

    @Override
    public void logInfo(String message) {
        System.out.println(message);
    }
}
