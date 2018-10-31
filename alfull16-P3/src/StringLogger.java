/*
 * File: StringLogger.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-4FallP1toP3 - EE333 Fall 2018
 * Vers: 1.0.1 09/15/2018 dgg - fix unchecked error re new ArrayList
 * Vers: 1.0.0 09/03/2018 dgg - initial coding
 */

import java.util.ArrayList;

/**
 * Log to a ArrayList
 * @author David Green DGreen@uab.edu
 */
public class StringLogger extends Logger {
    
    private ArrayList<String> logEntries;
    private int               threshold;

    /**
     * Create a string logger
     * @param threshold to meet or exceed for logging
     */
    public StringLogger(int threshold) {
        logEntries = new ArrayList<>();
        this.threshold = threshold;
    }
    
    public StringLogger(){
        this.logEntries = new ArrayList<>();
        this.threshold = Logger.DEBUG;
    }
    
    // queries
    
    /**
     * Retrieve the logEntries
     * @return logEntries made
     */
    public ArrayList<String> getLogEntries() {
        return logEntries;
    }
    
    // commands
    
    /**
     * Clear the log
     */
    public void clear() {
        logEntries.clear();
    }
    
    /**
     * Create an entry in the log if level exceeds logger's threshold
     * @param level entry's level
     * @param entry string text to log
     */
    @Override
    public void log(int level, String entry) {
        if (level >= threshold) {
            logEntries.add(entry);
        }
    }
    
}
