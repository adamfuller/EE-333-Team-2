/*
 * File: Room.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  Fall2018P1toP3 - EE333 Fall 2018
 * Vers: 1.1.3 10/10/2018 alf - added blower support 
 * Vers: 1.1.2 10/09/2018 alf - added MissingComponentException throws
 * Vers: 1.1.1 09/25/2018 dgg - add a toString() method
 * Vers: 1.1.0 09/02/2018 dgg - P2 changes
 * Vers: 1.0.1 09/03/2018 dgg - renamed Clock to clock per Java Naming Stds
 * Vers: 1.0.0 08/18/2018 dgg - initial coding
 */

/**
 * Model a room that has a controlled heater.  The room
 * models the impact of external factors as disturbances that
 * are furnished when the room is constructed.
 * 
 * @author David Green DGreen@uab.edu
 */
public class Room implements Clockable, Saveable{
    
    private Heater             heater;
    private TempSensor         tempSensor;
    private Blower             blower;
    
    private double[]           disturbance;
    private double             roomTemp;
    private int                dIndex;
    private final static int   HOT_AIR  =  95;
    // Constructors    
    
    /**
     * Create a room with disturbance array
     * @param  tempDisturbance temperature disturbance array 
     * @param  initialTemp starting room temperature
     */
    public Room(double[] tempDisturbance, double initialTemp) {
        disturbance = tempDisturbance;
        dIndex      = 0;
        roomTemp    = initialTemp;
    }
    
    public Room(){
        this.disturbance = new double[]{};
        dIndex = 0;
        roomTemp = 70.0;
    }
    
    // Queries
    
    /**
     * Get the present room temperature
     * @return temperature in degrees F
     */
    public double getTemp() {
        return roomTemp;
    }
    
    // Setters
    
    public void setTemp(double temp){
        this.roomTemp = temp;
    }
    
    public void setDisturbance(double[] d){
        this.disturbance = d;
    }
    
    // Commands
    
    /**
     * Add temperature sensor to the room
     * @param ts temperature sensor to add
     */
    public void add(TempSensor ts) {
        tempSensor = ts;
    }
    
    /**
     * Add heater to the room
     * @param  htr heater to add
     */
    public void add(Heater htr) {
        heater = htr;
    }
    
    /**
     * Add blower to the room
     * @param  blower blower to add
     */
    public void add(Blower blower) {
        this.blower = blower;
    }
    
    /**
     * Do any pre-clock actions (compute new room temperature)
     * @throws MissingComponentException if the Room is missing
     * a blower, heater, or tempSensor this is thrown
     */
    @Override
    public void preClock() throws MissingComponentException{

        if (this.blower == null || this.heater == null || this.tempSensor == null){
            throw new MissingComponentException("Room is missing a component "
                    + "in preClock");
        }
        
        if ( heater.getState() && blower.getState() ) {
            roomTemp = (roomTemp + disturbance[dIndex] + HOT_AIR ) / 3.;
        } else {
            roomTemp = (roomTemp + disturbance[dIndex]           ) / 2.;
        }
        tempSensor.setTemp(roomTemp);
    }
    
    /**
     * Move to next disturbance point (loop at end of array)
     */
    @Override
    public void clock() {
        dIndex++;
        if (dIndex >= disturbance.length) {
            dIndex = 0;
        }
    }
    
    /**
     * Provide a string for Room which will be stable for unit tests
     */
    @Override
    public String toString() {
        return "Room";
    }
}
