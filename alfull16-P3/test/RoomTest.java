/*
 * File: RoomTest.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-4FallP1toP3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Room
 * @author David Green DGreen@uab.edu
 */
public class RoomTest {
    
    private Room       room;
    private TempSensor ts1;
    private Heater     h1;
    private Blower     b1;
    
    private double[]   disturb        = {65,   65,    65,    65,   65,    65,    65,   65,    
            65,    65,   65,    65,    65,   65,    73,    73,    73,    73,    73,    65    };
    private boolean[]  heaterState    = {true, false, false, true, false, false, true, false,
            false, true, false, false, true, false, false, false, false, false, false, false };
    private double[]   expectedTempBlowerOff = { 67.5, 66.25,65.625,65.3125,65.15625,
        65.078125,65.0390625,65.01953125,65.009765625,65.0048828125,65.00244140625,
        65.001220703125,65.0006103515625,65.00030517578125,69.00015258789062,
        71.00007629394531,72.00003814697266,72.50001907348633, 72.75000953674316,
        68.87500476837158
    };
    private double[]   expectedTemp   = {67.5, 75.8,  70.4,  67.7, 75.9,  70.5,  67.7, 75.9,
            70.5,  67.7, 75.9,  70.5,  67.7, 75.9,  74.5,  73.7,  73.4,  73.2,  73.1,  69.0  };
    
    private static final double INITIAL_TEMP = 70.0;

    public RoomTest() {
    }

    @Before
    public void setUp() {
        room = new Room(disturb, INITIAL_TEMP );
        ts1  = new TempSensor(null);
        h1   = new Heater(null);
        b1 = new Blower();
        
        room.add(ts1);
        room.add(h1);
        room.add(b1);
    }

    /**
     * Test of getTemp method, of class Room.
     */
    @Test
    public void testInitialGetTemp() {
        assertEquals(INITIAL_TEMP, room.getTemp(), .001);
    }
    
    @Test
    public void testExceptions(){
        double d[] = {0.0};
        Room r = new Room(d, 70);
        try{
            r.preClock();
            assert(false);
        } catch (MissingComponentException e){
            assert(true);
        }
        
    }

    /**
     * Test of preClock method, of class Room.
     */
    @Test
    public void testRoomDynamics() {
        for ( int i = 0; i < disturb.length; i++) {
            try{
                room.preClock();
            } catch(MissingComponentException e){
                    
            }
            room.clock();
            h1.setState(heaterState[i]);
            assertEquals(expectedTempBlowerOff[i], ts1.getTemp(), .1 );
        }
    }

}