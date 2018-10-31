/*
 * File: Clock.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-1FallP1-3 - EE333 Fall 2018
 * Vers: 1.0.1 09/15/2018 dgg - fixed unchecked warning re new ArrayList
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

import java.util.ArrayList;

/**
 * Model a clock, it will call preClock, then clock of registered items.
 * 
 * On preClock, registered items are expected to compute things for use by the 
 * controller assuming the controller's outputs are constant for one clock (1 second).
 * 
 * On clock, registered items are expected to compute new control results and
 * set any outputs (to the room).
 * 
 * @author David Green DGreen@uab.edu
 */
public class Clock implements Saveable{

    private ArrayList<Clockable> clockableObjects;
    private long                 clockCount         = 0;
    private Logger               logger;
    
    /**
     * Create a clock with a null logger
     */
    public Clock() {
        setMeta(new NullLogger());
    }
    
    /**
     * Create a clock with a specified logger.
     * 
     * @param logger logger to use.  If null, a NullLogger will be created and used
     */
    public Clock(Logger logger) {
        setMeta(logger);
    }
    
    // Info that all constructors use
    private void setMeta(Logger aLogger) {
        clockableObjects = new ArrayList<>();
        logger = aLogger;
    }
    
    /**
     * Add a clockable object to the list of items to be clocked
     * @param item clockable object
     */
    public void add(Clockable item) {
        if (item != null) {
            clockableObjects.add(item);
        }
    }
    
    /**
     * preClock then clock all items
     * @throws MissingComponentException pass on from clockable
     */
    public void run() throws MissingComponentException{
           
        clockCount++;
        logger.log(Logger.TIMESTAMP, "--- Clocking to " + clockCount + " seconds.");
        
        // preClock
        for (Clockable object: clockableObjects){
            logger.log(Logger.INFO, "Preclocking " + object);
            object.preClock();
        }
        
        // Clock
        clockableObjects.forEach((object)->{
            logger.log(Logger.INFO, "Clocking " + object);
            object.clock();
        });
    }
    
    /**
     * Run n times
     * @param n number of times to preClock then clock
     * @throws MissingComponentException passed on from clockable
     */
    public void run(int n) throws MissingComponentException{
        for (int i = 0; i < n; i++) {
            run();
        }
    }
    
}
