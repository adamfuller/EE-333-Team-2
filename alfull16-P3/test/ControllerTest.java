/*
 * File: ControllerTest.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-4FallP1toP3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 * Vers: 1.0.1 10/11/2018 alf - added blower support
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Controller operation (without logging)
 *
 * @author David Green DGreen@uab.edu
 */
public class ControllerTest {

    private Heater     h1;
    private TempSensor ts1;
    private Controller controller;
    private Blower blower;
    
    public ControllerTest() {
    }

    @Before
    public void setUp() {
        h1         = new Heater(null);
        ts1        = new TempSensor(null);
        controller = new Controller(null);
        blower = new Blower();
    }

    /**
     * Test of toString method, of class Controller.
     */
    @Test
    public void testToString() {
        String expectedValue;
                
        expectedValue = "Controller with no temperature sensor and no heater";
        assertEquals(expectedValue, controller.toString());
        
        controller.connect(h1);
        expectedValue = "Controller with no temperature sensor and " + h1.toString();
        assertEquals(expectedValue, controller.toString());

        controller.connect(ts1);
        expectedValue = "Controller with " + ts1.toString() + " and " + h1.toString();
        assertEquals(expectedValue, controller.toString());
        
    }
    
    @Test
    public void testConnect(){
        assertEquals(controller.hasBlower(), false);
        controller.connect(blower);
        assertEquals(controller.hasBlower(), true);
    }
    
    @Test
    public void testExceptions(){
        Controller c2 = new Controller();
        try{
            c2.preClock();
            assert(false);
        } catch (MissingComponentException e){
            assert(true);
        }
    }
    
    @Test
    public void testDelay(){
        Controller c = new Controller();
        Blower b = new Blower();
        Heater h = new Heater();
        double d[] = {65.0};
        Room r = new Room(d, 65.0);
        TempSensor t = new TempSensor();
        Clock cl = new Clock();
        
        r.add(b);
        r.add(h);
        r.add(t);
        c.connect(b);
        c.connect(h);
        c.connect(t);
        
        cl.add(r);
        cl.add(c);
        
        try{
            cl.run();
            assert(h.getState());
            assert(!b.getState());
            cl.run();
            assert(h.getState());
            assert(!b.getState());
            cl.run();
            assert(h.getState());
            assert(b.getState());
        } catch (MissingComponentException e){}
        
    }

    /**
     * Test of clock method, of class Controller.
     */
    @Test
    public void testOperation() {
        
        double[]  temps    = { 65.0, 67.0, 68.0, 70.0, 71.0,  72.0,  71.0,  68.0, 67.0 };
        boolean[] expected = { true, true, true, true, true, false, false, false, true };
        
        controller.connect(h1);
        controller.connect(ts1);
        controller.connect(blower);
        
        for( int i = 0; i < temps.length; i++ ) {
            ts1.setTemp(temps[i]);
            
            try{
                controller.preClock();
            } catch(MissingComponentException e){
                assert(false);
            }
            
            controller.clock();
            assertEquals(expected[i], h1.getState());
        }
    }

}
