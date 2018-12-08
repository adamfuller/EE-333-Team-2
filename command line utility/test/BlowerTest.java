/*
 * File: BlowerTest.java
 * Author: Adam Fuller alfull16@uab.edu
 * Assignment:  P3 - EE333 Fall 2018
 * Vers: 1.0.0 10/09/2018 alf - initial coding
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alfuller
 */
public class BlowerTest {
    private Blower blower;
    
    
    
    @Before
    public void setup(){
        this.blower = new Blower();
    }
    
    /**
     * Test the getter and setter for the state of the blower
     */
    @Test
    public void testSetAndGetState(){
        assertEquals(this.blower.getState(), false);
        this.blower.setState(true);
        assertEquals(this.blower.getState(), true);
    }
    
    /**
     * Test the blower's ability to add a heater
     */
    @Test
    public void testAdd(){
        this.blower.add(new Heater());
        assertEquals(this.blower.isConnected(), true);
    }
    
    
}
