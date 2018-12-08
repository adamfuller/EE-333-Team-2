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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class HVAC extends EzJPanel {
    private static int floor = 0;
    private static int room = 0;
    private ArrayList<ArrayList<Room>> floors;
    private Timer clock;
    private Clock displayClock;
    static public int xPos = 200;
    static public int yPos = 200;
    static public int xVel = 20;
    static public int yVel = 20;

    // V2 Components
    private JComboBox roomPicker, floorPicker;
    private JSlider roomTempSlider, outsideTempSlider;
    private JButton startStopButton;
    private JLabel roomTempLabel, outsideTempLabel;
    private JRadioButton heatingOnJRadioButton, heatingOffJRadioButton;
    private JRadioButton coolingOnJRadioButton, coolingOffJRadioButton;

    public HVAC() {
        super(640, 480, "HVAC Helper");
        super.jf.setMinimumSize(new Dimension(480, 400));

        updateTemp();
        updateHeatingCooling();
        stopLooping();
    }

    @Override
    public void setup() {
        this.floors = new ArrayList<ArrayList<Room>>();
        this.displayClock = new Clock();

        Room myRoom = new Room(60.0 / 280.0, 20.0 / 180.0, 80.0 / 280.0, 60.0 / 180.0, 70.1);
        Room firstFloorBathRoom = new Room(140.0 / 280.0, 20.0 / 180.0, 40.0 / 280.0, 20.0 / 180.0, 70.1);
        Room redRoom = new Room(20.0 / 280.0, 80.0 / 180.0, 40.0 / 280.0, 80.0 / 180.0, 70.1);
        Room livingRoom = new Room(60.0 / 280.0, 80.0 / 180.0, 100.0 / 280.0, 80.0 / 180.0, 70.1);
        Room diningRoom = new Room(180.0 / 280.0, 100.0 / 180.0, 80.0 / 280.0, 60.0 / 180.0, 70.1);
        Room kitchen = new Room(180.0 / 280.0, 20.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.1);

        Room savRoom = new Room(60.0 / 280.0, 20.0 / 180.0, 80 / 280.0, 60.0 / 180.0, 70.1);
        Room charlieRoom = new Room(60.0 / 280.0, 80.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.1);
        Room brettRoom = new Room(140.0 / 280.0, 120.0 / 180.0, 40.0 / 280.0, 40.0 / 180.0, 70.1);
        Room secondFloorBathRoom = new Room(140.0 / 280.0, 20.0 / 180.0, 60.0 / 280.0, 40.0 / 180.0, 70.1);
        Room haydenRoom = new Room(200.0 / 280.0, 20.0 / 180.0, 60.0 / 280.0, 60.0 / 180.0, 70.1);
        Room emptyRoom = new Room(180.0 / 280.0, 80.0 / 180.0, 80.0 / 280.0, 80.0 / 180.0, 70.1);

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
            this.repaint();
        });

        this.clock.start();

    }

    @Override
    protected void addComponents() {
        jf.add(this, BorderLayout.CENTER);
        // super.addKeyListener(this);

        jf.add(getRightControls(), BorderLayout.EAST);
        jf.add(getBottomControls(), BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu help = new JMenu("Help Me");
        help.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                System.out.println("Help clicked");

                JDialog dialog = new JDialog();
                dialog.setSize(200, 200);
                dialog.setLocation(500, 500);
                dialog.setLayout(new GridLayout(1, 1));
                JLabel helpLabel = new JLabel("It's just a heater stupid");
                helpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dialog.add(helpLabel);
                dialog.setVisible(true);
                dialog.setResizable(false);

                new Timer(30, (NULL) -> {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                    xPos += xVel;
                    yPos += yVel;
                    if (xPos >= screenSize.getWidth() - 200) {
                        xVel *= -1;
                    } else if (xPos <= 0) {
                        xVel *= -1;
                    }
                    if (yPos >= screenSize.getHeight() - 200) {
                        yVel *= -1;
                    } else if (yPos <= 0) {
                        yVel *= -1;
                    }
                    dialog.setLocation(xPos, yPos);
                }).start();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        help.addActionListener((ae) -> {
            // System.out.println("Help clicked");
        });

        menuBar.add(help);
        jf.setJMenuBar(menuBar);


    }

    // @Override
    // public void keyPressed(KeyEvent ke) {
    //     super.keyPressed(ke);
    //     System.out.println("Key Pressed");
    // }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        for (int i = 0; i < this.floors.get(HVAC.floor).size(); i++) {
            if (this.floors.get(HVAC.floor).get(i).isClicked(x, y)) {
                HVAC.room = i;
            }
        }
        this.roomPicker.setSelectedIndex(HVAC.room);
        // System.out.println("Room Pressed: " + HVAC.room);
        updateHeatingCooling();
        updateTemp();
        this.repaint();
    }

    // #region Componenets V2

    /**
     * Get the controls to the right of the graphics window
     * @return {@code JPanel} containing the controls on the right
     */
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
            this.repaint();
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
            this.repaint();
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
            this.repaint();
        });

        this.heatingOffJRadioButton = new JRadioButton("Off");
        this.heatingOffJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.heatingOffJRadioButton.setSelected(true);
        this.heatingOffJRadioButton.addActionListener((ae) -> {
            this.heatingOnJRadioButton.setSelected(!heatingOffJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setHeating(!this.heatingOffJRadioButton.isSelected());
            this.repaint();
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
            this.repaint();
        });

        this.coolingOffJRadioButton = new JRadioButton("Off");
        this.coolingOffJRadioButton.setSelected(true);
        this.coolingOffJRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.coolingOffJRadioButton.addActionListener((ae) -> {
            this.coolingOnJRadioButton.setSelected(!coolingOffJRadioButton.isSelected());
            this.floors.get(HVAC.floor).get(HVAC.room).setCooling(!this.coolingOffJRadioButton.isSelected());
            this.repaint();
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

    /**
     * Get the controls below the graphics window
     * @return {@code JPanel} containing the controls on the bottom
     */
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
            repaint();
        });

        // Create outside temp area
        this.outsideTempLabel = new JLabel("Outside Temperature ("+(int) Room.getOutsideTemp()+"°F)");
        this.outsideTempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.outsideTempSlider = new JSlider(30, 110, (int) Room.getOutsideTemp());
        this.outsideTempSlider.setMinorTickSpacing(1);
        this.outsideTempSlider.setMajorTickSpacing(10);
        this.outsideTempSlider.setPaintTicks(true);
        this.outsideTempSlider.setPaintLabels(true);

        this.outsideTempSlider.addChangeListener((ce) -> {
            JSlider slider = (JSlider) ce.getSource();
            Room.setOutsideTemp(slider.getValue() * 1.0);
            this.outsideTempLabel.setText("Outside Temperature (" + slider.getValue() + "°F)");
        });

        JLabel credits = new JLabel(
                "HVAC Helper (Trial Version)    Developers: Adam Fuller, Isaiah Chamoun, and Lawrence Oldham");
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

    /**
     * Update the heating and cooling radio buttons
     */
    private void updateHeatingCooling() {
        if (HVAC.room >= floors.get(floor).size()) {
            this.heatingOffJRadioButton.setSelected(true);
            this.heatingOnJRadioButton.setSelected(false);
            this.coolingOnJRadioButton.setSelected(false);
            this.coolingOffJRadioButton.setSelected(true);
        } else {
            boolean roomIsHeating = this.floors.get(HVAC.floor).get(HVAC.room).isHeating();
            boolean roomIsCooling = this.floors.get(HVAC.floor).get(HVAC.room).isCooling();

            if (roomIsHeating != this.heatingOnJRadioButton.isSelected()){
                this.heatingOnJRadioButton.setSelected(roomIsHeating);
                this.heatingOffJRadioButton.setSelected(!roomIsHeating);
            }
            if (roomIsCooling != this.coolingOnJRadioButton.isSelected()){
                this.coolingOnJRadioButton.setSelected(roomIsCooling);
                this.coolingOffJRadioButton.setSelected(!roomIsCooling);
            }
        }
    }

    /**
     * Update the room temp sliders
     */
    private void updateTemp() {
        if (this.roomTempSlider.getValue() != ((int) this.floors.get(HVAC.floor).get(HVAC.room).getTemp())){
            this.roomTempSlider.setValue((int) this.floors.get(HVAC.floor).get(HVAC.room).getTemp() );
        }
        this.roomTempLabel.setText("Room Temperature (" + this.roomTempSlider.getValue() + "°F)");
        // repaint();
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
        updateHeatingCooling();
    }

    public static void main(String args[]) {
        try { // use the native menu bar on a mac
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        } catch (SecurityException e) {
            /* probably running via webstart, do nothing */
        }
        HVAC hvac = new HVAC();
    }


    /**
     * Class for the display clock on the screen
     */
    private class Clock {
        private double angle1 = -1.0 * Math.PI / 2.0;
        private double angle1Increment = Math.PI / 30.0;
        private double radius = 20.0;

        public Clock() {

        }

        public void tick() {
            this.angle1 += angle1Increment;
        }

        public void draw(Graphics g) {
            g.setColor(Color.white);
            g.drawArc(1, 1, (int) (2 * radius), (int) (2 * radius), 0, 360);
            g.drawLine((int) (1 + radius), (int) (1 + radius), (int) (1 + radius + radius * Math.cos(angle1)),
                    (int) (1 + radius + radius * Math.sin(angle1)));

        }

    }

}