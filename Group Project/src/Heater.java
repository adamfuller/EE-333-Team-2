/*
 * File: Heater.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  Fall2018P1toP3 - EE333 Fall 2018
 * Vers: 1.1.2 10/11/2018 alf - added default constructor
 * Vers: 1.1.1 09/25/2018 dgg - update documentation for toString
 * Vers: 1.1.0 09/02/2018 dgg - changes for P2
 * Vers: 1.0.0 08/18/2018 dgg - initial coding
 */

/**
 *
 * @author David Green DGreen@uab.edu
 */
public class Heater {

    private static long UIDSource = 20000;
    
    private long    UID;        // Unique ID
    private boolean state;      // State of heater
    private Logger  logger;     // Logger object

    // Constructors
    public Heater(){
        UID = UIDSource++;
        state = false;
        this.logger = new NullLogger();
    }

    /**
     * Create a heater that needs to be configured
     * @param logger logger to use
     */
    public Heater(Logger logger) {
        UID = UIDSource++;
        state = false;
        this.logger = (logger != null) ? logger : new NullLogger();
    }

    // Queries

    /**
     * Get the Heater's UID
     * @return UID
     */
    public long getUID() {
        return UID;
    }
    
    /**
     * Get the state of the header
     * @return true if heater is on otherwise false 
     */
    public boolean getState() {
        return state;
    }
    
    /**
     * returns the string “Heater:{UID} = {state}”
     * example: <code>Heater:20000 = ON</code>
     * @return formatted string
     */
    @Override
    public String toString() {
        return "Heater:" + UID + " = " + (state ? "ON" : "OFF" );
    }

    // Commands

    /**
     * Set heater's state
     * @param state true to turn heater ON, false for OFF
     */
    public void setState(boolean state) {
        if ( this.state != state ) {
            logger.log(Logger.INFO, "" + this + ", turning " + ( (state) ? "on" : "off") );
        }
        this.state = state;
    }
}
