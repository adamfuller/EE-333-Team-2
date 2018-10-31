/*
 * File: TempSensor.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  Fall2018P1toP3 - EE333 Fall 2018
 * Vers: 1.0.1 10/11/2018 alf - added default constructor
 * Vers: 1.0.0 08/18/2018 dgg - initial coding
 */

/**
 * TempSensors are unique and consist of a unique ID assigned at creation 
 * (starting at 10000 and incremented by 1 for each creation), 
 * and sense a temperature. A temperature sensor will initially indicate 
 * a temperature of <code>NaN</code>.
 * @author David Green DGreen@uab.edu
 */
public class TempSensor implements Saveable {

    private static long UIDSource = 10000;
    
    private long    UID;        // Unique ID
    private double  temp;       // Temperature of sensor
    private Logger  logger;     // Logger object

    // Constructors
    
    /**
     * Create an temperature sensor
     */
    TempSensor() {
        UID = UIDSource++;
        temp = Double.NaN;
        this.logger = new NullLogger();
    }
    /**
     * 
     * @param logger logger to use
     */
    TempSensor(Logger logger) {
        UID = UIDSource++;
        temp = Double.NaN;
        this.logger = (logger != null) ? logger : new NullLogger();
    }

    // Queries
    
    /**
     * Get the UID for the sensor
     *
     * @return sensor UID
     */
    public long getUID() {
        return UID;
    }

    /**
     * get sensor's temperature
     *
     * @return temperature in degrees F
     */
    public double getTemp() {
        return temp;
    }

    /**
     * returns the string “TS:{UID} = {temperature}” example:
     * <code>TS:10000 = 75.0</code>
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return "TS:" + UID + " = " + temp;
    }

    // Commands
    
    /**
     * Set the temperature of the sensor
     *
     * @param temperature new value to be considered the sensed temperature in
     * degrees F
     */
    public void setTemp(double temperature) {
        temp = temperature;
        logger.log(Logger.INFO, this + " (set)");
    }
}
