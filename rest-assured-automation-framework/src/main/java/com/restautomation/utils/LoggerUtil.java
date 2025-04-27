package com.restautomation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoggerUtil - Utility class for logging
 */
public class LoggerUtil {
    private static final Logger LOGGER = LogManager.getLogger(LoggerUtil.class);

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Log information
     * @param message log message
     */
    public static void info(String message) {
        LOGGER.info(message);
    }

    /**
     * Log information with parameters
     * @param message log message with placeholders
     * @param args arguments for placeholders
     */
    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }

    /**
     * Log warning
     * @param message log message
     */
    public static void warn(String message) {
        LOGGER.warn(message);
    }

    /**
     * Log warning with parameters
     * @param message log message with placeholders
     * @param args arguments for placeholders
     */
    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    /**
     * Log error
     * @param message log message
     */
    public static void error(String message) {
        LOGGER.error(message);
    }

    /**
     * Log error with parameters
     * @param message log message with placeholders
     * @param args arguments for placeholders
     */
    public static void error(String message, Object... args) {
        LOGGER.error(message, args);
    }

    /**
     * Log error with exception
     * @param message log message
     * @param throwable exception
     */
    public static void error(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    /**
     * Log debug
     * @param message log message
     */
    public static void debug(String message) {
        LOGGER.debug(message);
    }

    /**
     * Log debug with parameters
     * @param message log message with placeholders
     * @param args arguments for placeholders
     */
    public static void debug(String message, Object... args) {
        LOGGER.debug(message, args);
    }
}