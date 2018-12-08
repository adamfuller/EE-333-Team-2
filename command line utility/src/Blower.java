/*
 * File: Blower.java
 * Author: Adam Fuller alfull16@uab.edu
 * Assignment:  P3 - EE333 Fall 2018
 * Vers: 1.0.0 10/09/2018 alf - initial coding
 */

public class Blower implements Saveable{
    private Heater heater;
    private boolean state = false;
    private Logger logger;
    
    /**
     * Create a Blower object
     */
    public Blower(){
        this.logger = new NullLogger();
    }
    
    /**
     * 
     * @param logger logger to log to during state changes
     */
    public Blower(Logger logger){
        this.logger = logger;
    }
    
    /**
     * Connects a heater
     * 
     * @param heater heater to connect
     */
    public void add(Heater heater){
        this.heater = heater;
        this.logger.log(Logger.INFO, "Added heater to Blower");
    }
    
    /**
     * Determine if this blower is connected
     * to a heater or not
     * 
     * @return boolean if this blower is connected or not
     */
    public boolean isConnected(){
        return this.heater!=null;
    }
    
    /**
     * Turns on or off the blower depending on {@code state}
     * 
     * @param state new state of the blower
     */
    public void setState(boolean state){
        this.state = state;
        this.logger.log(Logger.INFO, state?"Blower turned on":"Blower turned off");
    }
    
    /**
     * Get the current state of the blower
     * 
     * @return boolean of if the blower is on or off
     */
    public boolean getState(){
        return this.state;
    }
    
    
}
