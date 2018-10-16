/*
 * File: Clockable.java
 * Author: David Green DGreen@uab.edu
 * Assignment:  2018-1FallP1-3 - EE333 Fall 2018
 * Vers: 1.0.0 09/02/2018 dgg - initial coding
 */

/**
 * Interface for objects that are to be clocked to get 1 second notifications
 *
 * @author David Green DGreen@uab.edu
 */
public interface Clockable {

    /**
     * Take actions based on notification that the clock is about to happen. The
     * controller's latest actions were issued one second ago. Generally used to
     * compute dynamics before letting new control decisions occur.
     * @throws MissingComponentException exception to notify of a missing component
     */
    public void preClock() throws MissingComponentException;

    /**
     * Take actions for new second. Generally used to allow the controller to
     * make a new decision.
     */
    public void clock();
}
