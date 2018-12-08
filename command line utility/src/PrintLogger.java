/*
 * File: PrintLogger.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-1FallP1-3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

/**
 * Attachable conditional printing to stdout
 * 
 * PrintLogger implements a concept of a level where nothing will print if the 
 * level of the request does not equal or exceed the level at which the 
 * printLogger is operating at.
 * 
 * @author David Green dgreen@uab.edu
 */
public class PrintLogger extends Logger implements Saveable {
        
    /**
     * Create a PrintLogger object at DEBUG level
     */
    public PrintLogger() {
        this.threshold = DEBUG;
    }

    /**
     * Create a PrintLogger object at the specified level
     * @param threshold specified level for print logger
     */
    public PrintLogger(int threshold) {
        this.threshold = threshold;
    }
    
/**
 * Prints a string to stdout if level is greater than or equal to threshold.  
 * The printing routine will add a newline to the logEntry.
 * The Logger class defines common values for level.
 * @param level    value noting the type of the information
 * @param logEntry text to be part of the printed log
 */
    @Override
    public void log(int level, String logEntry) {
        
        if (level >= threshold) {
            System.out.println(logEntry);
        }
    }
}
