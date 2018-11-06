/*
* File: Controller.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  Fall2018P1toP3 - EE333 Fall 2018
 * Vers: 1.2.1 10/11/2018 alf - added delay between heater and blower
 * Vers: 1.2.0 10/10/2018 alf - added blower support
 * Vers: 1.1.1 10/09/2018 alf - added MissingComponentException throws
 * Vers: 1.1.0 09/02/2018 dgg - implement P2 changes
 * Vers: 1.0.1 09/06/2018 dgg - Explicitly declare the initial controller command
 *                              to heater (to be OFF) and turn the heater off as
 *                              it is connected.
 * Vers: 1.0.0 08/19/2018 dgg - initial coding

 */
/**
 * The controller connects to one temperature sensor and one heater.
 * The controller will turn on the heater when the temperature is below 68.0.
 * The controller will turn off the heater when the temperature is above 71.0 degrees.
 *
 * @author David Green DGreen@uab.edu
 */
public class Controller implements Clockable, Saveable{

    private TempSensor tempSensor;         // input to controller
    private Heater heater;             // heat output of controller
    private Blower blower;
    private boolean presentHeatState;   // present command to heater

    // Configuration 
    private final double LOW_HEAT_TEMP = 68.0;
    private final double HIGH_HEAT_TEMP = 71.0;

    private Logger logger;             // logger 
    private int blowerDelayCount = 0;

    // Constructors
    /**
     * Create a controller
     */
    public Controller() {
        this.logger = new NullLogger();
        presentHeatState = false;
    }

    /**
     * Create a controller
     *
     * @param logger logger to be used
     */
    public Controller(Logger logger) {
        this.logger = (logger != null) ? logger : new NullLogger();
        presentHeatState = false;
    }

    // Queries
    /**
     * Provide the string “Controller with TS:{UID} = {temperature} and
     * Heater:{UID} = {state}” example:
     * <code>Contoller with TS:10000 = 75.0 and Heater:20000 = ON</code> Use "no
     * xyz" if there is no corresponding object
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        String tempSensorString = (tempSensor == null) ? "no temperature sensor"
                : tempSensor.toString();
        String heaterString = (heater == null) ? "no heater"
                : heater.toString();

        return "Controller with " + tempSensorString + " and " + heaterString;
    }

    // Commands
    /**
     * Connect the temperature sensor to the controller. Only one connection at
     * a time is possible. The method will silently overwrite on multiple
     * requests.
     *
     * @param ts temperature sensor
     */
    public void connect(TempSensor ts) {
        tempSensor = ts;
        logger.log(Logger.INFO, "Connect Temperature Sensor " + ts);
    }

    /**
     * Connect the heater to the controller. Only one connection at a time is
     * possible. The method will silently overwrite on multiple requests.
     *
     * @param heater heater to connect
     */
    public void connect(Heater heater) {
        this.heater = heater;
        this.heater.setState(false);
        logger.log(Logger.INFO, "Connect Heater " + heater);
    }

    /**
     * Connect the blower to the controller Only one connection at a time is
     * possible. The method will silently overwrite on multiple requests.
     *
     * @param blower blower to connect
     */
    public void connect(Blower blower) {
        this.blower = blower;
        if (this.heater == null) {
            this.blower.setState(false);
        } else {
            this.blower.setState(this.heater.getState());
        }
    }

    /**
     * Determine if a blower is connected
     *
     * @return boolean of if this Controller has a blower or not
     */
    public boolean hasBlower() {
        return this.blower != null;
    }

    /**
     * Do actions before clock (none needed yet)
     */
    @Override
    public void preClock() throws MissingComponentException {
        if (this.tempSensor == null || this.heater == null || this.blower == null) {
            throw new MissingComponentException("Controller preclock called"
                    + " with missing component");
        }
    }

    /**
     * Do one pass of the controller (read the temperature, determine whether to
     * turn heater on or off, and then do it)
     */
    @Override
    public void clock() {
        double temp = tempSensor.getTemp();

        boolean s = (temp < LOW_HEAT_TEMP)
                || (presentHeatState && !(temp > HIGH_HEAT_TEMP));

        heater.setState(s);

        if (this.heater.getState()) {
            this.blower.setState(true);
            System.out.println("Turned blower on");
        } else if (this.heater.getState()) {
            this.blowerDelayCount++;
        } else {
            this.blowerDelayCount = 0;
            this.blower.setState(false);
        }

        presentHeatState = s;
        logger.log(Logger.INFO, "Temperature is " + temp + ", set heater to " + s);
    }

}
