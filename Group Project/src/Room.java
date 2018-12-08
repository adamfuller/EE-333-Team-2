/*
 * File: Room.java
 * Authors: Adam Fuller, Isaiah Chamoun, Lawrence Oldham
 * Assignment:  Group Project - EE333 Fall 2018
 * Vers: 1.0.1 11/28/2018 alf - updated for percentage based drawing
 * Vers: 1.0.0 11/13/2018 alf - initial coding
 */
import java.awt.Color;
import java.awt.Graphics;

public class Room {
    private static double outsideTemp = 70.0;
    private static double coolingTemp = 55.0;
    private static double heatingTemp = 85.0;
    private static double tempRate = 0.08;
    private static double minTemp = 30.0;
    private static double maxTemp = 110.0;

    private int x, y, width, height;
    private double xP, yP, widthP, heightP, temp;
    private double switchingTemp = 70.0;
    private double switchingRange = 1.0;
    private boolean isHeating = false;
    private boolean isCooling = false;
    private boolean userSet = false;
    private boolean isLocked = false; // maybe same as userSet?

    public Room(int x, int y, int width, int height, double temp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.temp = temp;
    }

    /**
     * Create new room where location and size is a fraction/decimal from 0.0-1.0
     */
    public Room(double xP, double yP, double widthP, double heightP, double temp) {
        this.xP = xP;
        this.yP = yP;
        this.widthP = widthP;
        this.heightP = heightP;
        this.temp = temp;
    }

    /**
     * Simulate one clock cycle
     */
    public void tick() {
        if (!this.userSet) {
            if (this.temp > this.switchingTemp + switchingRange) {
                this.isCooling = true;
                this.isHeating = false;
            } else if (this.temp < this.switchingTemp - switchingRange) {
                this.isHeating = true;
                this.isCooling = false;
            }
            else {
                this.isCooling = false;
                this.isHeating = false;
            }
        }
        if (this.isCooling) {
            this.setTemp((this.temp + Room.coolingTemp * Room.tempRate + Room.outsideTemp * Room.tempRate)
                    / ((1 + 2 * Room.tempRate)));
        } else if (this.isHeating) {
            this.setTemp((this.temp + Room.heatingTemp * Room.tempRate + Room.outsideTemp * Room.tempRate)
                    / (1 + 2 * Room.tempRate));
        } else {
            this.setTemp((this.temp + Room.outsideTemp * Room.tempRate) / (1 + Room.tempRate));
        }
    }

    /**
     * Update the rooms temp
     * 
     * @param temp
     */
    public void setTemp(double temp) {
        if (temp > Room.maxTemp) {
            temp = Room.maxTemp;
        } else if (temp < Room.minTemp) {
            temp = Room.minTemp;
        }
        this.temp = temp;
    }
    

    /**
     * Returns the locked state of the room
     * 
     * @return
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Returns if the room is heating or not
     * 
     * @return
     */
    public boolean isHeating() {
        return this.isHeating;
    }

    /**
     * Returns if the room is cooling or not
     * 
     * @return
     */
    public boolean isCooling() {
        return this.isCooling;
    }

    /**
     * Gets the temperature of the room
     * 
     * @return the current temperature of this room
     */
    public double getTemp() {
        return this.temp;
    }

    /**
     * Returns the x position of the room
     * 
     * @return
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y position of the room
     * 
     * @return
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get temperature outside of the building
     */
    public static double getOutsideTemp() {
        return Room.outsideTemp;
    }

    /**
     * Update the outside temp
     * 
     * @param outsideTemp
     */
    public static void setOutsideTemp(double outsideTemp) {
        Room.outsideTemp = outsideTemp;
    }

    /**
     * User updates the cooling seting
     * 
     * @param state
     */
    public void setCooling(boolean state) {
        this.isCooling = state;
        this.userSet = true;
    }

    /**
     * User updates the heating setting
     * 
     * @param state
     */
    public void setHeating(boolean state) {
        this.isHeating = state;
        this.userSet = true;
    }

    /**
     * Returns the current color of the room
     * 
     * @return
     */
    public Color getColor() {
        return this.getColor((int) this.temp, (int) Room.minTemp, (int) Room.maxTemp);
    }

    /**
     * Returns color based on closeness to min and max
     * 
     * @param val
     * @param min
     * @param max
     * @return {@code Color} of the room
     */
    private Color getColor(int val, int min, int max) {
        int r = 0;
        int g = 0;
        int b = 0;
        int mid = (max + min) / 2;

        if (val > mid) {
            r = (int) map(val * 1.0, mid * 1.0, max * 1.0, 0, 254);
            g = (int) map(val * 1.0, mid * 1.0, max * 1.0, 254, 0);
        } else if (val < mid) {
            g = (int) map(val * 1.0, min * 1.0, mid * 1.0, 0, 254);
            b = (int) map(val * 1.0, min * 1.0, mid * 1.0, 254, 0);
        } else {
            g = 254;
        }
        return new Color(r, g, b);
    }

    /**
     * Returns a linear interpolation between two numbers, {@code minNum} and
     * {@code maxNum}, to two other numbers {@code minMap} and {@code maxMap}
     * 
     * @param num
     * @param minNum
     * @param maxNum
     * @param minMap
     * @param maxMap
     * @return {@code double} value interpolated between minMap and maxMap
     */
    public static double map(double num, double minNum, double maxNum, double minMap, double maxMap) {
        /*
         * double slope = (maxMap - minMap)/(maxNum - minNum); double dif1 = (num -
         * minNum); double move = slope*dif1;
         */
        double allInOne = ((maxMap - minMap) / (maxNum - minNum)) * (num - minNum) + minMap;
        // y = (y2 - y1)/(x2 - x1) * (x-x1) + y1
        return (allInOne);
    }

    /**
     * Draw this room
     * @param g graphics object to be drawn on
     */
    public void draw(Graphics g) {
        g.setColor(this.getColor((int) this.temp, (int) Room.minTemp, (int) Room.maxTemp));
        g.fillRect(this.x, this.y, this.width, this.height);
        if (this.isCooling) {
            g.setColor(Color.BLUE);
            g.fillRect(this.x + this.width - 5, this.y, 5, 5);
        } else if (this.isHeating) {
            g.setColor(Color.RED);
            g.fillRect(this.x + this.width - 5, this.y, 5, 5);
        }

        g.setColor(Color.black);
        g.drawRect(this.x, this.y, this.width, this.height);
    }

    /**
     * Draws based on input percentages
     * 
     * @param g
     * @param windowWidth
     * @param windowHeight
     */
    public void drawP(Graphics g, int windowWidth, int windowHeight) {
        g.setColor(this.getColor((int) this.temp, (int) Room.minTemp, (int) Room.maxTemp));
        this.x = (int) (this.xP * windowWidth);
        this.y = (int) (this.yP * windowHeight);
        this.width = (int) (this.widthP * windowWidth);
        this.height = (int) (this.heightP * windowHeight);

        g.fillRect(this.x, this.y, this.width, this.height);
        if (this.isCooling) {
            g.setColor(Color.BLUE);
            g.fillRect(this.x + this.width - 10, this.y, 10, 10);
        } else if (this.isHeating) {
            g.setColor(Color.RED);
            g.fillRect(this.x + this.width - 10, this.y, 10, 10);
        }

        g.setColor(Color.black);
        g.drawRect(this.x, this.y, this.width, this.height);
    }

    /**
     * Returns if a click happens on this room
     * 
     * @param clickX
     * @param clickY
     * @return {@code true} if the click is within bounds of this room
     */
    public boolean isClicked(int clickX, int clickY) {
        if (clickX > this.x && clickX < this.x + this.width) {
            if (clickY > this.y && clickY < this.y + this.height) {
                return true;
            }
        }
        return false;
    }

}