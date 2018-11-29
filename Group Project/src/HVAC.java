/*
 * File: HVAC.java
 * Authors: Adam Fuller, Isaiah Chamoun, Lawrence Oldham
 * Assignment:  Group Project - EE333 Fall 2018
 * Vers: 2.0.0 11/28/2018 alf - switched to revised design and functionality
 * Vers: 1.0.0 10/23/2018 alf - initial coding
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class HVAC extends EzJPanel {
    private static int floor = 0;
    private static int room = 0;
    private ArrayList<ArrayList<Room>> floors;
    private Timer clock;
    private Clock displayClock;
    

    // V2 Components
    private JComboBox floorPicker;
    private JComboBox roomPicker;
    private JSlider roomTempSlider;
    private JSlider outsideTempSlider;
    private JButton startStopButton;
    private JLabel roomTempLabel;
    private JLabel outsideTempLabel;
    private JRadioButton heatingOnJRadioButton;
    private JRadioButton heatingOffJRadioButton;
    private JRadioButton coolingOnJRadioButton;
    private JRadioButton coolingOffJRadioButton;

    public HVAC() {
        super(640, 480, "HVAC Helper");
        super.jf.setMinimumSize(new Dimension(400, 400));
        
        updateTemp();
        updateHeatingCoolingLocked();
    }

    @Override
    public void setup() {
        this.floors = new ArrayList<ArrayList<Room>>();
        this.displayClock = new Clock();

        Room myRoom = new Room(60.0 / 280.0, 20.0 / 180.0, 80.0 / 280.0, 60.0 / 180.0, 70.0);
        Room firstFloorBathRoom = new Room(140.0 / 280.0, 20.0 / 180.0, 40.0 / 280.0, 20.0 / 180.0, 70.0);
        Room redRoom = new Room(20.0 / 280.0, 80.0 / 180.0, 40.0 / 280.0, 80.0 / 180.0, 70.0);
        Room livingRoom = new Room(60.0 / 280.0, 80.0 / 180.0, 100.0 / 280.0, 80.0 / 180.0, 70.0);
        Room diningRoom = new Room(180.0 / 280.0, 100.0 / 180.0, 80.0 / 280.0, 60.0 / 180.0, 70.0);
        Room kitchen = new Room(180.0 / 280.0, 20.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.0);

        Room savRoom = new Room(60.0 / 280.0, 20.0 / 180.0, 80 / 280.0, 60.0 / 180.0, 70.0);
        Room charlieRoom = new Room(60.0 / 280.0, 80.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.0);
        Room brettRoom = new Room(140.0 / 280.0, 120.0 / 180.0, 40.0 / 280.0, 40.0 / 180.0, 70.0);
        Room secondFloorBathRoom = new Room(140.0 / 280.0, 20.0 / 180.0, 60.0 / 280.0, 40.0 / 180.0, 70.0);
        Room haydenRoom = new Room(200.0 / 280.0, 20.0 / 180.0, 60.0 / 280.0, 60.0 / 180.0, 70.0);
        Room emptyRoom = new Room(180.0 / 280.0, 80.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.0);

        // First floor stuff
        ArrayList<Room> firstFloor = new ArrayList<>();
        firstFloor.add(myRoom);
        firstFloor.add(firstFloorBathRoom);
        firstFloor.add(redRoom);
        firstFloor.add(livingRoom);
        firstFloor.add(diningRoom);
        firstFloor.add(kitchen);
        this.floors.add(firstFloor);

        // Second floor stuff
        ArrayList<Room> secondFloor = new ArrayList<>();
        secondFloor.add(savRoom);
        secondFloor.add(charlieRoom);
        secondFloor.add(brettRoom);
        secondFloor.add(secondFloorBathRoom);
        secondFloor.add(haydenRoom);
        secondFloor.add(emptyRoom);
        this.floors.add(secondFloor);

        this.clock = new Timer(1000, (NULL) -> {
            this.floors.forEach((floor) -> {
                floor.forEach((room) -> {
                    room.tick();
                });
            });
            this.displayClock.tick();
        });

        this.clock.start();

    }

    @Override
    protected void addComponents() {

        jf.add(this, BorderLayout.CENTER);
        jf.addMouseListener(this);
        jf.addKeyListener(this);

        jf.add(getRightControls(), BorderLayout.EAST);
        jf.add(getBottomControls(), BorderLayout.SOUTH);

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        super.keyPressed(ke);
        // System.out.println("Key Pressed");
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int width = super.windowWidth;
        int height = super.windowHeight;
        int x = me.getX();
        int y = me.getY();
        for (int i = 0; i < this.floors.get(HVAC.floor).size(); i++) {
            if (this.floors.get(HVAC.floor).get(i).isClicked(x, y)) {
                HVAC.room = i;
            }
        }
        this.roomPicker.setSelectedIndex(HVAC.room);
        // System.out.println("Room Pressed: " + HVAC.room);
        updateHeatingCoolingLocked();
        updateTemp();
    }

    // #region Componenets V2

    public JPanel getRightControls() {
        JPanel rightJPanel = new JPanel();
        // rightJPanel.setLayout(new GridLayout(12, 1));
        rightJPanel.setLayout(new BoxLayout(rightJPanel, BoxLayout.Y_AXIS));
        rightJPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create floorPicker
        String floorLabels[] = new String[this.floors != null ? floors.size() : 1];
        for (int i = 0; i < (this.floors != null ? floors.size() : 1); i++) {
            floorLabels[i] = "Floor " + String.valueOf(i + 1);
        }
        this.floorPicker = new JComboBox<String>(floorLabels);
        this.floorPicker.setAlignmentY(Component.TOP_ALIGNMENT);
        this.floorPicker.addActionListener((ae) -> {
            JComboBox combo = (JComboBox) ae.getSource();
            HVAC.floor = combo.getSelectedIndex();
            HVAC.room = 0;
            this.roomPicker.setSelectedIndex(0);
        });

        // Create roomPicker
        ArrayList<Room> rooms = this.floors != null ? this.floors.get(HVAC.floor) : new ArrayList<Room>();

        String roomLabels[] = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            roomLabels[i] = "Room " + String.valueOf(i + 1);
        }
        this.roomPicker = new JComboBox<String>(roomLabels);
        this.roomPicker.setAlignmentY(Component.TOP_ALIGNMENT);
        this.roomPicker.addActionListener((ae) -> {
            JComboBox combo = (JComboBox) ae.getSource();
            HVAC.room = combo.getSelectedIndex();
        });

        // Create start stop button
        this.startStopButton = new JButton("Start/Stop");
        this.startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension d = new Dimension(100, 50);
        this.startStopButton.setMinimumSize(d);
        startStopButton.addActionListener((e) -> {
            if (this.clock.isRunning()) {
                this.clock.stop();
            } else {
                this.clock.start();
            }
        });

        // create Heating Radio button section
        JLabel heatingLabel = new JLabel("Heating");
        heatingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        heatingLabel.setFont(heatingLabel.getFont().deriveFont(Font.BOLD));

        this.heatingOnJRadioButton = new JRadioButton("On");
        this.heatingOnJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.heatingOnJRadioButton.addActionListener((ae) -> {
            this.heatingOffJRadioButton.setSelected(!heatingOnJRadioButton.isSelected());
            this.coolingOnJRadioButton.setSelected(false);
            this.coolingOffJRadioButton.setSelected(true);
            this.floors.get(HVAC.floor).get(HVAC.room).setHeating(this.heatingOnJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setCooling(false);
        });

        this.heatingOffJRadioButton = new JRadioButton("Off");
        this.heatingOffJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.heatingOffJRadioButton.addActionListener((ae) -> {
            this.heatingOnJRadioButton.setSelected(!heatingOffJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setHeating(!this.heatingOffJRadioButton.isSelected());
        });

        // create Cooling Radio button section
        JLabel coolingLabel = new JLabel("Cooling");
        coolingLabel.setFont(coolingLabel.getFont().deriveFont(Font.BOLD));
        coolingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.coolingOnJRadioButton = new JRadioButton("On");
        this.coolingOnJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.coolingOnJRadioButton.addActionListener((ae) -> {
            this.coolingOffJRadioButton.setSelected(!coolingOnJRadioButton.isSelected());
            this.heatingOnJRadioButton.setSelected(false);
            this.floors.get(HVAC.floor).get(HVAC.room).setCooling(this.coolingOnJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setHeating(false);
        });

        this.coolingOffJRadioButton = new JRadioButton("Off");
        this.coolingOffJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.coolingOffJRadioButton.addActionListener((ae) -> {
            this.coolingOnJRadioButton.setSelected(!coolingOffJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setCooling(!this.coolingOffJRadioButton.isSelected());
        });

        rightJPanel.add(this.roomPicker);
        rightJPanel.add(this.floorPicker);
        rightJPanel.add(this.startStopButton);
        rightJPanel.add(Box.createVerticalStrut(10));
        rightJPanel.add(heatingLabel);
        rightJPanel.add(this.heatingOnJRadioButton);
        rightJPanel.add(this.heatingOffJRadioButton);
        rightJPanel.add(Box.createVerticalStrut(10));
        rightJPanel.add(coolingLabel);
        rightJPanel.add(this.coolingOnJRadioButton);
        rightJPanel.add(this.coolingOffJRadioButton);
        return rightJPanel;
    }

    public JPanel getBottomControls() {
        JPanel bottomJPanel = new JPanel();
        bottomJPanel.setLayout(new BoxLayout(bottomJPanel, BoxLayout.Y_AXIS));
        bottomJPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create room temp area
        this.roomTempLabel = new JLabel("Room Temperature (30-110°F)");
        this.roomTempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.roomTempSlider = new JSlider(30, 110, (int) this.floors.get(HVAC.floor).get(HVAC.room).getTemp());
        this.roomTempSlider.setMinorTickSpacing(1);
        this.roomTempSlider.setMajorTickSpacing(10);
        this.roomTempSlider.setPaintTicks(true);
        this.roomTempSlider.setPaintLabels(true);

        this.roomTempSlider.addChangeListener((ce) -> {
            JSlider slider = (JSlider) ce.getSource();
            this.floors.get(HVAC.floor).get(HVAC.room).setTemp(slider.getValue() * 1.0);
        });

        // Create outside temp area
        this.outsideTempLabel = new JLabel("Outside Temperature (30-110°F)");
        this.outsideTempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.outsideTempSlider = new JSlider(30, 110, (int) Room.getOutsideTemp());
        this.outsideTempSlider.setMinorTickSpacing(1);
        this.outsideTempSlider.setMajorTickSpacing(10);
        this.outsideTempSlider.setPaintTicks(true);
        this.outsideTempSlider.setPaintLabels(true);

        this.outsideTempSlider.addChangeListener((ce) -> {
            JSlider slider = (JSlider) ce.getSource();
            Room.setOutsideTemp(slider.getValue() * 1.0);
            this.outsideTempLabel.setText("Outside Temperature ("+slider.getValue()+"°F)");
        });

        JLabel credits = new JLabel("Developers: Adam Fuller, Isaiah Chamoun, and Lawrence Oldham");
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);
        credits.setFont(credits.getFont().deriveFont(10.0f));
        credits.setForeground(Color.GRAY);

        // add all components
        bottomJPanel.add(roomTempLabel);
        bottomJPanel.add(this.roomTempSlider);
        bottomJPanel.add(outsideTempLabel);
        bottomJPanel.add(this.outsideTempSlider);
        bottomJPanel.add(credits);
        return bottomJPanel;
    }

    // #endregion Components V2

    private void updateHeatingCoolingLocked() {
        if (HVAC.room >= floors.get(floor).size()) {
            this.heatingOffJRadioButton.setSelected(true);
            this.heatingOnJRadioButton.setSelected(false);
            this.coolingOnJRadioButton.setSelected(false);
            this.coolingOffJRadioButton.setSelected(true);
        } else {
            this.heatingOnJRadioButton.setSelected(this.floors.get(HVAC.floor).get(HVAC.room).isHeating());
            this.heatingOffJRadioButton.setSelected(!this.floors.get(HVAC.floor).get(HVAC.room).isHeating());
            this.coolingOnJRadioButton.setSelected(this.floors.get(HVAC.floor).get(HVAC.room).isCooling());
            this.coolingOffJRadioButton.setSelected(!this.floors.get(HVAC.floor).get(HVAC.room).isCooling());
        }
    }

    private void updateTemp() {
        this.roomTempSlider.setValue((int) this.floors.get(HVAC.floor).get(HVAC.room).getTemp());

        this.roomTempLabel.setText("Room Temperature (" + this.roomTempSlider.getValue()+ "°F)");
        
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < this.floors.get(HVAC.floor).size(); i++) {
            Room r = this.floors.get(HVAC.floor).get(i);
            r.drawP(g, getWidth(), getHeight());
            g.drawString("" + (i + 1), r.getX() + 3, r.getY() + 15);
        }
        this.displayClock.draw(g);
        // this.floors.get(HVAC.floor).forEach((r)->{
        // r.drawP(g, getWidth(), getHeight());
        // g.drawString(""+this.floors.indexOf(r)+1, r.getX(), r.getY());
        // });
        updateTemp();
        updateHeatingCoolingLocked();
    }

    public static void main(String args[]) {
        // try {
        //     // System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "Ted" );
        //     System.setProperty( "com.apple.macos.useScreenMenuBar", "true" );
        //     System.setProperty( "apple.laf.useScreenMenuBar", "true" ); // for older versions of Java
        //   } catch ( SecurityException e ) {
        //     /* probably running via webstart, do nothing */
        // }
        HVAC hvac = new HVAC();
    }

    private class Clock{
        private double angle1 = -1.0* Math.PI/2.0;
        private double angle1Increment = Math.PI/30.0;
        private double length1 = 9.0;
        private double radius = 20.0;

        public Clock(){

        }

        public void tick(){
            this.angle1+=angle1Increment;
        }

        public void draw(Graphics g){
            g.setColor(Color.white);
            g.drawArc(1, 1, (int) (2*radius), (int) (2*radius), 0, 360);
            g.drawLine( (int) (1+radius), (int) (1+radius), (int) (1+radius+radius*Math.cos(angle1)), (int) (1+radius+radius*Math.sin(angle1)));

            
        }

    }

}
