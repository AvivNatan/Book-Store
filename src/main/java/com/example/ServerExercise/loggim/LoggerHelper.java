package com.example.ServerExercise.loggim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.MDC;

public class LoggerHelper
{
    private int requestNumber = 0;
    private long startTimeCurrentReq;
    private static final Logger logRequests = LoggerFactory.getLogger("request-logger");
    private static final Logger logBooks = LoggerFactory.getLogger("books-logger");

    public void addRequest() {
        this.requestNumber++;
        MDC.put("requestNumber", String.valueOf(this.requestNumber));
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void addInfoLogForLogRequests(String resourceName, String httpVerb) {
        logRequests.info("Incoming request | #{} | resource: {} | HTTP Verb {}",
                this.requestNumber, resourceName, httpVerb);
    }

    public void addDebugLogForLogRequests() {
        long endTimeCurrentReq = System.currentTimeMillis();
        logRequests.debug("request #{} duration: {}ms",
                this.requestNumber, endTimeCurrentReq - this.startTimeCurrentReq);
    }

    public void initStartTimeCurrentReq() {
        this.startTimeCurrentReq = System.currentTimeMillis();
    }

    public void addLogsToCreateBookEndPoint(String newBookTitle, int totalBooksWithoutCurrent, int newBookId) {
        logBooks.info("Creating new Book with Title [{}]", newBookTitle);
        logBooks.debug("Currently there are {} Books in the system. New Book will be assigned with id {}",
                totalBooksWithoutCurrent, newBookId);
    }

    public void addLogToBookDetailsEndPoint(int bookId) {
        logBooks.debug("Fetching book id {} details", bookId);
    }

    public void addLogToBooksCountAndDetailsEndPoint(int totalBooksFound) {
        logBooks.info("Total Books found for requested filters is {}", totalBooksFound);
    }

    public void addLogsToUpdateBookPriceEndPoint(int bookId, String bookTitle, int oldPrice, int newPrice) {
        logBooks.info("Update Book id [{}] price to {}", bookId, newPrice);
        logBooks.debug("Book [{}] price change: {} --> {}", bookTitle, oldPrice, newPrice);
    }

    public void addLogsToDeleteBookEndPoint(int bookId, String bookTitle, int totalRemainingBooks) {
        logBooks.info("Removing book [{}]", bookTitle);
        logBooks.debug("After removing book [{}] id: [{}] there are {} books in the system",
                bookTitle, bookId, totalRemainingBooks);
    }

    public void addLogError(String message) {
        logBooks.error(message);
    }

    public String getLevelOfLogger(String loggerName) throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        if (logger == null) {
            throw new Exception("The logger-name doesn't exist");
        }
        Level level = logger.getLevel();
        if (level == null) {
            throw new Exception("Log level is not set");
        }
        return level.toString().toUpperCase();
    }

    public String setLevelOfLogger(String loggerName, String loggerLevel) throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        if (logger == null) {
            throw new Exception("The logger-name doesn't exist");
        }
        Level newLevel = Level.toLevel(loggerLevel.toUpperCase(), null);
        if (newLevel == null) {
            throw new Exception("Invalid logger-level");
        }
        logger.setLevel(newLevel);
        return newLevel.toString().toUpperCase();
    }
}
