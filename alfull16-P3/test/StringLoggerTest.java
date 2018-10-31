/*
 * File: StringLoggerTest.java
 * Author: Adam Fuller alfull16@uab.edu
 * Assignment:  P3 - EE333 Fall 2018
 * Vers: 1.0.0 10/09/2018 alf - initial coding
 */

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alfuller
 */
public class StringLoggerTest {
    StringLogger stringLogger;
    public StringLoggerTest(){
        
    }
    
    @Before
    public void setup(){
        this.stringLogger = new StringLogger(Logger.INFO);
        this.stringLogger.log(Logger.ALWAYS, "Hello");
    }
    
    /**
     * Test the log method and the getLogEntries method of
     * the StringLogger class
     */
    @Test
    public void testLogAndGetLogs(){
        this.stringLogger.log(Logger.DEBUG, "GoodBye");
        ArrayList<String> entries = this.stringLogger.getLogEntries();
        assertEquals(entries.size(), 1);
    }
    
    /**
     * Test the clear method of StringLogger class
     */
    @Test
    public void testClear(){
        this.stringLogger.clear();
        assertEquals(this.stringLogger.getLogEntries().size(), 0);
    }
    
}
