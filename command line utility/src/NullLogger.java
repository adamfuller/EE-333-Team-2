/*
 * File: NullLogger.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-1FallP1-3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

/**
 * Implements the Logger interface but quietly ignores all commands
 * @author David Green DGreen@uab.edu
 */
public class NullLogger extends Logger implements Saveable {

    /**
     * Ignore request to log
     * @param level     unused
     * @param logEntry  unused
     */
    @Override
    public void log(int level, String logEntry) {
    }
}
