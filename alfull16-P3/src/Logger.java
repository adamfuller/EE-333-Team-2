/*
 * File: Logger.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-1FallP1-3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

/**
 * Abstract Super Class for loggers
 *
 * @author David Green dgreen@uab.edu
 */
public abstract class Logger {

    // Some useful definitions
    /**
     * DEBUG level -- most verbose, lots of details when searching for a bug
     */
    public static final int DEBUG = 0;
    /**
     * INFO level -- information that is routine operation
     */
    public static final int INFO = 10;
    /**
     * TIMESTAMP level -- periodic time stamp
     */
    public static final int TIMESTAMP = 20;
    /**
     * WARNING level -- information that is not normal but which it is believe
     * that the code can accommodate the situation correctly although the user
     * may want to know for future reasons
     */
    public static final int WARNING = 50;
    /**
     * ERROR level -- information that is about an error that impacts the proper
     * operation of the program
     */
    public static final int ERROR = 100;
    /**
     * ALWAYS level -- always print the information
     */
    public static final int ALWAYS = 100000;

    protected int threshold;

    /**
     * Default Constructor
     */
    public Logger() {

    }

    /**
     * Log a message if `level` is greater than or equal to logger's threshold.
     * The actual logging routine will add a newline to the logEntry if
     * appropriate.
     *
     * @param level message's level
     * @param logEntry text to log (a newline will be added if appropriate)
     */
    public abstract void log(int level, String logEntry);

    /**
     * Set a new log threshold for actual logging
     *
     * @param newThreshold level that must be met or exceeded for actual logging
     * when the logger is asked to log something
     */
    public void setLogThreshold(int newThreshold) {
        threshold = newThreshold;
    }

}
