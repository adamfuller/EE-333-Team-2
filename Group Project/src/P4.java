/*  TODO:
    Fix click location to work with new scaling
    Map room's color between max and min temp on the floor
        - Fix max/min temperature bug with excesively high/low numbers
        
*/
/* TODONE:
    √ Add border to rooms
    √ Fix window resizing bug where rooms disappear
 */
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class P4 extends EzJPanel {
    private static int floor = 1;
    private static int room = 1;
    private ArrayList<ArrayList<Room>> floors = new ArrayList<ArrayList<Room>>();
    private JTextArea floorValue;
    private JTextArea roomValue;
    private JTextArea tempValue;
    private JSlider floorSlider;
    private JRadioButton heatingJRadioButton;
    private JRadioButton coolingJRadioButton;
    private JRadioButton lockedJRadioButton;

    public P4() {
        super(450, 500, "P4");
        ArrayList<Room> floor1 = new ArrayList<>();
        floor1.add(new Room());
        this.floors.add(floor1);
        updateTemp();
        updateHeatingCoolingLocked();
    }

    @Override
    protected void addComponents() {

        jf.add(this, BorderLayout.CENTER);
        jf.addMouseListener(this);

        jf.add(getFloorSlider(), BorderLayout.EAST);
        jf.add(getControls(), BorderLayout.SOUTH);

    }

    @Override
    public void mousePressed(MouseEvent me){
        int width = super.windowWidth;
        int height = super.windowHeight;
        int numRooms = 0;
        for (Room room: floors.get(floor-1)){
            numRooms++;
        }
        int size;
        size = (numRooms==0)?0:(int) ((height)/(Math.ceil(Math.sqrt(numRooms))));
        int numCols = width/size;
        // numRows = height/size;

        int col = (int) Math.floor( (double) (me.getX()*1.0/size));
        int row = (int) Math.floor( (double) (me.getY()*1.0/size));
        int index = col+row*numCols; // room number clicked on;

        System.out.println("Col: " + col + " Row: " + row + " Index: " + index);
        if (index >= floors.get(floor-1).size()){
            return;
        } else {
            P4.room = index + 1;
            this.roomValue.setText(String.valueOf(P4.room));
            updateHeatingCoolingLocked();
            updateTemp();
        }

    }

    private JSlider getFloorSlider() {
        this.floorSlider = new JSlider(JSlider.VERTICAL, 1, 1, 1);
        // this.floorSlider.setPaintLabels(true);
        // this.floorSlider.setPaintTrack(true);
        // this.floorSlider.setPaintTicks(true);
        this.floorSlider.addChangeListener(new ChangeListener() {

            @Override

            public void stateChanged(ChangeEvent e) {
                P4.room = 1;
                P4.floor = floorSlider.getValue();
                floorValue.setText(String.valueOf(P4.floor));
                System.out.println(floorSlider.getValue());
                updateHeatingCoolingLocked();
                repaint();
            }
        });
        return floorSlider;
    }

    private JPanel getControls() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(1, 0));
        jPanel.setBorder(new EmptyBorder(8, 10, 8, 8));

        jPanel.add(getFloorAndRoom(), BorderLayout.LINE_START);
        jPanel.add(getHeatingCoolingLocked(), BorderLayout.CENTER);
        jPanel.add(getTempAddRoomAndFloor(), BorderLayout.LINE_END);

        return jPanel;
    }

    private JPanel getFloorAndRoom() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        JPanel floorPanel = new JPanel();
        floorPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        floorPanel.setLayout(new BorderLayout());
        JTextArea floorLabel = new JTextArea("Floor:  ");
        floorLabel.setEditable(false);
        floorLabel.setBackground(new Color(0, 0, 0, 0));

        this.floorValue = new JTextArea(1, 5);
        this.floorValue.setText("1");
        this.floorValue.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                String floorText = floorValue.getText();
                if (floorText.equals("")) {
                    return;
                } else {
                    try {
                        Integer.parseInt(floorText);
                        P4.room = Integer.parseInt(floorText);
                        System.out.println(P4.room);
                    } catch (Exception ex) {
                        P4.room = 1;
                        floorValue.setText("1");
                    }
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String floorText = floorValue.getText();

                try {
                    Integer.parseInt(floorText);
                    P4.room = Integer.parseInt(floorText);
                    System.out.println(P4.room);
                } catch (Exception ex) {
                    P4.room = 1;
                    floorValue.setText("1");
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String floorText = floorValue.getText();

                try {
                    Integer.parseInt(floorText);
                    P4.room = Integer.parseInt(floorText);
                    System.out.println(P4.room);
                } catch (Exception ex) {
                    floorValue.setText("");
                }
                updateHeatingCoolingLocked();
            }
        });

        floorPanel.add(floorLabel, BorderLayout.WEST);
        floorPanel.add(this.floorValue, BorderLayout.EAST);

        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        JTextArea roomLabel = new JTextArea("Room:  ");
        roomLabel.setEditable(false);
        roomLabel.setBackground(new Color(0, 0, 0, 0));
        this.roomValue = new JTextArea(1, 5);
        this.roomValue.setText("1");
        this.roomValue.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                String roomText = roomValue.getText();
                if (roomText.equals("")) {
                    return;
                } else {
                    try {
                        Integer.parseInt(roomText);
                        P4.room = Integer.parseInt(roomText);
                        System.out.println(P4.room);
                    } catch (Exception ex) {
                        P4.room = 1;
                        roomValue.setText("1");
                    }
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                String roomText = roomValue.getText();

                try {
                    Integer.parseInt(roomText);
                    P4.room = Integer.parseInt(roomText);
                    System.out.println(P4.room);
                } catch (Exception ex) {
                    P4.room = 1;
                    roomValue.setText("1");
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String roomText = roomValue.getText();

                try {
                    Integer.parseInt(roomText);
                    P4.room = Integer.parseInt(roomText);
                    System.out.println(P4.room);
                } catch (Exception ex) {
                    roomValue.setText("");
                }
                updateHeatingCoolingLocked();
            }
        });

        roomPanel.add(roomLabel, BorderLayout.WEST);
        roomPanel.add(this.roomValue, BorderLayout.EAST);

        jPanel.add(floorPanel, BorderLayout.NORTH);
        jPanel.add(roomPanel, BorderLayout.SOUTH);

        return jPanel;
    }

    private JPanel getHeatingCoolingLocked() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(new EmptyBorder(0, 25, 0, 0));

        heatingJRadioButton = new JRadioButton("Heating");
        coolingJRadioButton = new JRadioButton("Cooling");
        lockedJRadioButton = new JRadioButton("Locked");

        heatingJRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if (heatingJRadioButton.isSelected()){
                //     heatingJRadioButton.setSelected(false);
                // } else {
                //     heatingJRadioButton.setSelected(true);
                // }
                coolingJRadioButton.setSelected(false);
                
                // floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                // floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());

                if (room <= floors.get(floor-1).size()){
                    floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                    floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
                }
                repaint();
            }
        });

        coolingJRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if (coolingJRadioButton.isSelected()){
                //     coolingJRadioButton.setSelected(false);
                // } else {
                //     coolingJRadioButton.setSelected(true);
                // }
                heatingJRadioButton.setSelected(false);
                if (room <= floors.get(floor-1).size()){
                    floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                    floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
                }
                
                repaint();
            }
        });

        lockedJRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                floors.get(floor - 1).get(room - 1).setLocked(lockedJRadioButton.isSelected());
                repaint();
            }
        });

        jPanel.add(heatingJRadioButton, BorderLayout.NORTH);
        jPanel.add(coolingJRadioButton, BorderLayout.CENTER);
        jPanel.add(lockedJRadioButton, BorderLayout.SOUTH);

        return jPanel;
    }

    private JPanel getTempAddRoomAndFloor() {

        JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(0, 0, 0, 25));
        jPanel.setLayout(new BorderLayout());

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        JTextArea tempLabel = new JTextArea("Temp(°F):  ");
        tempLabel.setEditable(false);
        tempLabel.setBackground(new Color(0, 0, 0, 0));
        tempValue = new JTextArea(1, 5);

        try {
            tempValue.setText(String.valueOf(floors.get(floor).get(room).getTemp()));
        } catch (Exception e) {
            System.out.println("oops");
        }

        tempValue.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                // ignore this since it is removing the temperature
                
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                // update the rooms temperature to this
                Room r = floors.get(floor-1).get(room-1);
                try{
                    Double.parseDouble(tempValue.getText());
                    r.setTemp(Double.parseDouble(tempValue.getText()));
                } catch (Exception ex){

                }

                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // update the rooms temperature to this
                Room r = floors.get(floor-1).get(room-1);
                try{
                    Double.parseDouble(tempValue.getText());
                    r.setTemp(Double.parseDouble(tempValue.getText()));
                } catch (Exception ex){

                }
                repaint();
            }
        });

        tempPanel.add(tempLabel, BorderLayout.WEST);
        tempPanel.add(tempValue, BorderLayout.EAST);

        JButton addRoomJButton = new JButton("Add Room");
        addRoomJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add a new room here!
                // after adding the room allow the user to specify the temperature otherwise
                // use the average of the floor
                Room room = new Room();
                floors.get(floor-1).add(room);
                repaint();
            }
        });

        JButton addFloorJButton = new JButton("Add Floor");
        addFloorJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add a new Floor here!
                floors.add(new ArrayList<Room>());
                floorSlider.setMaximum(floors.size());
                System.out.println(floors.size());

            }
        });

        jPanel.add(tempPanel, BorderLayout.NORTH);
        jPanel.add(addRoomJButton, BorderLayout.CENTER);
        jPanel.add(addFloorJButton, BorderLayout.SOUTH);
        return jPanel;
    }

    private void updateHeatingCoolingLocked(){
        System.out.println("Floor: " + floor + " Room: " + room);
        if (room-1 >= floors.get(floor-1).size()){
            this.heatingJRadioButton.setSelected(false);
            this.coolingJRadioButton.setSelected(false);
            this.lockedJRadioButton.setSelected(false);
        } else {
            this.heatingJRadioButton.setSelected(this.floors.get(floor-1).get(room-1).isHeating());
            this.coolingJRadioButton.setSelected(this.floors.get(floor-1).get(room-1).isCooling());
            this.lockedJRadioButton.setSelected(this.floors.get(floor-1).get(room-1).isLocked());
        }
        
    }

    private void updateTemp(){
        this.tempValue.setText(String.valueOf(this.floors.get(floor-1).get(room-1).getTemp()));
    }

    public static double map(double num, double minNum, double maxNum, double minMap, double maxMap){
        return ((maxMap - minMap)/(maxNum - minNum))*(num - minNum) + minMap;
    }

    /**
     * Returns color based on closeness to min and max
     * 
     * @param val
     * @param min
     * @param max
     * @return
     */
    public Color getColor(int val, int min, int max){
        int r = 0;
        int g = 0;
        int b = 0;
        int mid = (max+min)/2;
        if (min == max){
            return new Color(0,254,0);
        }

        if (val > mid){
            r = (int) map(val*1.0, mid*1.0, max*1.0, 0, 254);
            g = (int) map(val*1.0, mid*1.0, max*1.0, 254, 0);
        } else if (val < mid){
            g = (int) map(val*1.0, min*1.0, mid*1.0, 0, 254);
            b = (int) map(val*1.0, min*1.0, mid*1.0, 254, 0);
        } else {
            g = 254;
        }
        return new Color(Math.abs(r),Math.abs(g),Math.abs(b));
    }

    @Override
    public void setup() {
        stopLooping();
    }


    @Override
    public void draw(Graphics g) {
        int width = super.windowWidth;
        int height = super.windowHeight;
        int numRooms = 0;
        double maxTemp = 0.0;
        double minTemp = 10000.0;

        // iterate through each room in the current floor
        for (Room room: floors.get(floor-1)){
            numRooms++;
            maxTemp = room.getTemp()>maxTemp?room.getTemp():maxTemp;
            minTemp = room.getTemp()<minTemp?room.getTemp():minTemp;
        }

        System.out.println("Max temp: " + maxTemp);
        System.out.println("Min Temp: " + minTemp);

        
        System.out.println("Num Rooms: " +numRooms);

        int size;

        if (numRooms == 0){
            size = 0;
        } else {
            size = height < width ? (int) ((height)/(Math.ceil(Math.sqrt(numRooms)))) :  (int) ((width)/(Math.ceil(Math.sqrt(numRooms))));
        }
        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        
        System.out.println("Size: " + size);
        
        

        int index;
        for (int j = 0; j < (height / size); j++) {
            for (int i = 0; i < width / size; i++) {
                index = i+j*width/size;
                ArrayList<Room> selectedFloor = floors.get(floor-1);
                // System.out.println("Selected floor size: " + selectedFloor.size());
                if (index < selectedFloor.size()){
                    

                    g.setColor(getColor( (int) floors.get(floor-1).get(index).getTemp(), (int) minTemp, (int) maxTemp)); //TODO map this color between min and max temp

                    g.fillRect(i*size,j*size, size, size);

                    if (floors.get(floor-1).get(index).isHeating()){
                        g.setColor(Color.red);
                    } else if (floors.get(floor-1).get(index).isCooling()){
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(Color.green);
                    }

                    g.fillRect(i*size+4*size/5, j*size, size/5, size/5);
                    
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(Math.round(floors.get(floor-1).get(index).getTemp()*100)/100d), i*size+size/2-20, j*size+size/2);
                    g.setColor(Color.black);
                    g.drawRect(i*size,j*size, size,size);
                }
                
            }
        }
    }

    public static void main(String args[]) {
        P4 p4 = new P4();
    }

    

}