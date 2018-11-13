/*  TODO:    
    Setup GUI to use command line tool
        - read all rooms for the current floor
        - run a few times a second from a timer
        
*/
/* TODONE:
    √ Add border to rooms
    √ Fix window resizing bug where rooms disappear
    √ Rename file since it isn't P4
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HVAC extends EzJPanel {
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
    // V2 Components
    private JComboBox floorPicker;
    private JComboBox roomPicker;

    public HVAC() {
        super(640, 480, "HVAC");
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
        jf.addKeyListener(this);

        // jf.add(getFloorSlider(), BorderLayout.EAST);
        // jf.add(getControls(), BorderLayout.SOUTH);
        jf.add(getRightControls(), BorderLayout.EAST);

    }
    
    @Override
    public void keyPressed(KeyEvent ke){
        super.keyPressed(ke);
        System.out.println("Key Pressed");
        
        if (HVAC.floor > 0 && HVAC.room > 0){
            if (ke.getKeyCode() == KeyEvent.VK_DELETE){
                this.floors.get(floor-1).remove(room-1);
                repaint();
            }
        }
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
        // size = (numRooms==0)?0:(int) ((height)/(Math.ceil(Math.sqrt(numRooms))));
        if (numRooms == 0){
            size = 0;
        } else {
            size = height < width ? (int) ((height)/(Math.ceil(Math.sqrt(numRooms)))) :  (int) ((width)/(Math.ceil(Math.sqrt(numRooms))));
        }
        int numCols = width/size;
        // numRows = height/size;

        int col = (int) Math.floor( (double) (me.getX()*1.0/size));
        int row = (int) Math.floor( (double) (me.getY()*1.0/size));
        int index = col+row*numCols; // room number clicked on;

        // System.out.println("Col: " + col + " Row: " + row + " Index: " + index);
        if (index >= floors.get(floor-1).size()){
            return;
        } else {
            HVAC.room = index + 1;
            this.roomValue.setText(String.valueOf(HVAC.room));
            updateHeatingCoolingLocked();
            updateTemp();
        }

    }

// #region Componenets V2

public JPanel getRightControls(){
    JPanel rightJPanel = new JPanel();
    rightJPanel.setLayout(new BoxLayout(rightJPanel, BoxLayout.Y_AXIS));
    

    // Create floorPicker
    String floorLabels[] = new String[this.floors!=null?floors.size():1];
    for (int i = 0; i<(this.floors!=null?floors.size():1); i++){
        floorLabels[i] = "Floor " + String.valueOf(i+1);
    }
    this.floorPicker = new JComboBox<String>(floorLabels);

    // Create roomPicker
    ArrayList<Room> rooms = this.floors!=null?this.floors.get(HVAC.floor-1):new ArrayList<Room>();
    if (rooms.size() == 0){
        rooms.add(new Room());
    }
    String roomLabels[] = new String[rooms.size()];
    for (int i = 0; i<rooms.size(); i++){
        roomLabels[i] = "Room " + String.valueOf(i+1);
    }
    this.roomPicker = new JComboBox<String>(roomLabels);



    rightJPanel.add(this.floorPicker);
    rightJPanel.add(this.roomPicker);
    rightJPanel.add(getButtons2());
    return rightJPanel;
}

private JPanel getButtons2() {

    JPanel jPanel = new JPanel();
    jPanel.setBorder(new EmptyBorder(0, 0, 0, 25));
    jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
    jPanel.setAlignmentX(50);

    JButton addRoomJButton = new JButton("Add Room");
    addRoomJButton.addActionListener((e)->{
         // add a new room here!
            // after adding the room allow the user to specify the temperature otherwise
            // use the average of the floor
            Room room = new Room();
            floors.get(floor-1).add(room);
            repaint();
    });

    JButton addFloorJButton = new JButton("Add Floor");
    addFloorJButton.addActionListener((e)->{
        // add a new Floor here!
        floors.add(new ArrayList<Room>());
        floorSlider.setMaximum(floors.size());
//                System.out.println(floors.size());
    });
//         addFloorJButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 // add a new Floor here!
//                 floors.add(new ArrayList<Room>());
//                 floorSlider.setMaximum(floors.size());
// //                System.out.println(floors.size());

//             }
//         });
    JButton startStopJButton = new JButton("Start/Stop");
    startStopJButton.addActionListener((e)->{
        // do something
    });
    

    // jPanel.add(tempPanel, BorderLayout.NORTH);
    jPanel.add(addFloorJButton);
    jPanel.add(addRoomJButton);
    jPanel.add(startStopJButton);
    return jPanel;
}

// #endregion Components V2

// #region components V1
    private JSlider getFloorSlider() {
        this.floorSlider = new JSlider(JSlider.VERTICAL, 1, 1, 1);
        // this.floorSlider.setPaintLabels(true);
        // this.floorSlider.setPaintTrack(true);
        // this.floorSlider.setPaintTicks(true);
        this.floorSlider.addChangeListener((e)->{
            HVAC.room = 1;
                HVAC.floor = floorSlider.getValue();
                floorValue.setText(String.valueOf(HVAC.floor));
//                System.out.println(floorSlider.getValue());
                updateHeatingCoolingLocked();
                repaint();
        });
//         this.floorSlider.addChangeListener(new ChangeListener() {

//             @Override

//             public void stateChanged(ChangeEvent e) {
//                 HVAC.room = 1;
//                 HVAC.floor = floorSlider.getValue();
//                 floorValue.setText(String.valueOf(HVAC.floor));
// //                System.out.println(floorSlider.getValue());
//                 updateHeatingCoolingLocked();
//                 repaint();
//             }
//         });
        return floorSlider;
    }

    private JPanel getControls() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        // jPanel.setBorder(new EmptyBorder(8, 10, 8, 8));

        jPanel.add(getTextInputs());
        jPanel.add(getRadioButtons());
        jPanel.add(getButtons());

        return jPanel;
    }

    private JPanel getTextInputs() {
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
                        HVAC.room = Integer.parseInt(floorText);
//                        System.out.println(HVAC.room);
                    } catch (Exception ex) {
                        HVAC.room = 1;
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
                    HVAC.room = Integer.parseInt(floorText);
//                    System.out.println(HVAC.room);
                } catch (Exception ex) {
                    HVAC.room = 1;
                    floorValue.setText("1");
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String floorText = floorValue.getText();

                try {
                    Integer.parseInt(floorText);
                    HVAC.room = Integer.parseInt(floorText);
//                    System.out.println(HVAC.room);
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
                        HVAC.room = Integer.parseInt(roomText);
//                        System.out.println(HVAC.room);
                    } catch (Exception ex) {
                        HVAC.room = 1;
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
                    HVAC.room = Integer.parseInt(roomText);
//                    System.out.println(HVAC.room);
                } catch (Exception ex) {
                    HVAC.room = 1;
                    roomValue.setText("1");
                }
                updateHeatingCoolingLocked();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String roomText = roomValue.getText();

                try {
                    Integer.parseInt(roomText);
                    HVAC.room = Integer.parseInt(roomText);
//                    System.out.println(HVAC.room);
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

    private JPanel getRadioButtons() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(new EmptyBorder(0, 25, 0, 0));

        heatingJRadioButton = new JRadioButton("Heating");
        coolingJRadioButton = new JRadioButton("Cooling");
        lockedJRadioButton = new JRadioButton("Locked");

        // heatingJRadioButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // if (heatingJRadioButton.isSelected()){
        //         //     heatingJRadioButton.setSelected(false);
        //         // } else {
        //         //     heatingJRadioButton.setSelected(true);
        //         // }
        //         coolingJRadioButton.setSelected(false);
                
        //         // floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
        //         // floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());

        //         if (room <= floors.get(floor-1).size()){
        //             floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
        //             floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
        //         }
        //         repaint();
        //     }
        // });
        heatingJRadioButton.addActionListener((e)->{
            coolingJRadioButton.setSelected(false);
                
                // floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                // floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());

                if (room <= floors.get(floor-1).size()){
                    floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                    floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
                }
                repaint();
        });

        coolingJRadioButton.addActionListener((e)->{
            heatingJRadioButton.setSelected(false);
            if (room <= floors.get(floor-1).size()){
                floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
                floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
            }
            repaint();
        });
        // coolingJRadioButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // if (coolingJRadioButton.isSelected()){
        //         //     coolingJRadioButton.setSelected(false);
        //         // } else {
        //         //     coolingJRadioButton.setSelected(true);
        //         // }
        //         heatingJRadioButton.setSelected(false);
        //         if (room <= floors.get(floor-1).size()){
        //             floors.get(floor - 1).get(room - 1).setHeating(heatingJRadioButton.isSelected());
        //             floors.get(floor - 1).get(room - 1).setCooling(coolingJRadioButton.isSelected());
        //         }
                
        //         repaint();
        //     }
        // });
        lockedJRadioButton.addActionListener((e)->{
            floors.get(floor - 1).get(room - 1).setLocked(lockedJRadioButton.isSelected());
            repaint();
        });
        // lockedJRadioButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         floors.get(floor - 1).get(room - 1).setLocked(lockedJRadioButton.isSelected());
        //         repaint();
        //     }
        // });

        jPanel.add(heatingJRadioButton, BorderLayout.NORTH);
        jPanel.add(coolingJRadioButton, BorderLayout.CENTER);
        jPanel.add(lockedJRadioButton, BorderLayout.SOUTH);

        return jPanel;
    }

    private JPanel getButtons() {

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
//            System.out.println("oops");
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
        addRoomJButton.addActionListener((e)->{
             // add a new room here!
                // after adding the room allow the user to specify the temperature otherwise
                // use the average of the floor
                Room room = new Room();
                floors.get(floor-1).add(room);
                repaint();
        });
        // addRoomJButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // add a new room here!
        //         // after adding the room allow the user to specify the temperature otherwise
        //         // use the average of the floor
        //         Room room = new Room();
        //         floors.get(floor-1).add(room);
        //         repaint();
        //     }
        // });

        JButton addFloorJButton = new JButton("Add Floor");
        addFloorJButton.addActionListener((e)->{
            // add a new Floor here!
            floors.add(new ArrayList<Room>());
            floorSlider.setMaximum(floors.size());
//                System.out.println(floors.size());
        });
//         addFloorJButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 // add a new Floor here!
//                 floors.add(new ArrayList<Room>());
//                 floorSlider.setMaximum(floors.size());
// //                System.out.println(floors.size());

//             }
//         });
        JButton startStopJButton = new JButton("Start/Stop");
        startStopJButton.addActionListener((e)->{
            // do something
        });
        

        // jPanel.add(tempPanel, BorderLayout.NORTH);
        jPanel.add(addRoomJButton, BorderLayout.CENTER);
        jPanel.add(addFloorJButton, BorderLayout.NORTH);
        jPanel.add(startStopJButton, BorderLayout.SOUTH);
        return jPanel;
    }
//#endregion Componenets V1

    private void updateHeatingCoolingLocked(){
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


        int size;

        if (numRooms == 0){
            size = 0;
        } else {
            size = height < width ? (int) ((height)/(Math.ceil(Math.sqrt(numRooms)))) :  (int) ((width)/(Math.ceil(Math.sqrt(numRooms))));
        }

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

    /**
     * Get the names of files for a floor number
     * @param floor - floor to get files for
     * @return ArrayList of filenames for the floor
     */
    public ArrayList<String> getFilenamesForFloor(int floor){
        ArrayList<String> filenames = new ArrayList<>();
        try {
            final File folder = new File(System.getProperty("user.dir"));
            for(File file: folder.listFiles()){
                if (file.getName().contains(String.valueOf(floor)) && file.getName().contains("Room") && file.getName().contains("Floor")){
                    filenames.add(file.getName());
                }
            }
        } catch (Exception e){
        }

        return filenames;
    }

    /**
     * 
     * @param command
     * @throws IOException
     */
    public String execute(String command) throws IOException{
        Process p = Runtime.getRuntime().exec(command.split(" "));
        String s = null;
        StringBuilder output = new StringBuilder();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = stdInput.readLine())!=null){
            output.append(s);
        }
        return output.toString();
    }

    public static void main(String args[]) {
        HVAC hvac = new HVAC();
    }

    

}
