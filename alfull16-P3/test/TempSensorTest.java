/*
 * File: TempSensorTest.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-4FallP1toP3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test TempSensors
 * @author David Green DGreen@uab.edu
 */
public class TempSensorTest {

    private TempSensor ts1;
    private TempSensor ts2;
    
    public TempSensorTest() {
    }

    @Before
    public void setUp() {
        ts1 = new TempSensor(null);
        ts2 = new TempSensor(null);
    }

    /**
     * Test of getUID method, of class TempSensor.
     */
    @Test
    public void testGetUID() {
        assertTrue(ts1.getUID() + 1 == ts2.getUID());
        assertTrue(ts1.getUID() >= 10000);
    }

    /**
     * Test of getTemp method, of class TempSensor.
     */
    @Test
    public void testTemp() {
        assertEquals(Double.NaN, ts1.getTemp(), .001);
        assertEquals("TS:" + ts1.getUID() + " = NaN", ts1.toString());

        ts1.setTemp(120.);
        assertEquals(120.,ts1.getTemp(), .001);
        String expectedValue = "TS:" + ts1.getUID() + " = " + 120.0;
        assertEquals(expectedValue, ts1.toString());
    }
}