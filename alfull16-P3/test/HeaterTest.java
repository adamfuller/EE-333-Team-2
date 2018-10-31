/*
 * File: HeaterTest.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-4FallP1toP3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Heater model
 * @author David Green DGreen@uab.edu
 */
public class HeaterTest {

    private Heater h1;
    private Heater h2;
    
    public HeaterTest() {
    }

    @Before
    public void setUp() {
        h1 = new Heater(null);
        h2 = new Heater(null);
    }

    /**
     * Test of getUID method, of class Heater.
     */
    @Test
    public void testGetUID() {
        assertTrue(h1.getUID() + 1 == h2.getUID());
        assertTrue(h1.getUID() >= 20000);
    }

    /**
     * Test of Heater Operation
     */
    @Test
    public void testHeaterOperation() {
        assertFalse(h1.getState());
        h1.setState(true);
        assertTrue(h1.getState());

        String h1Prefix = "Heater:" + h1.getUID();

        h1.setState(true);
        assertTrue(h1.getState());
        assertEquals(h1Prefix + " = ON",  h1.toString());
        
        h1.setState(false);
        assertFalse(h1.getState());
        assertEquals(h1Prefix + " = OFF", h1.toString());
    }

}