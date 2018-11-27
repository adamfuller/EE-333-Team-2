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


public class HVAC extends Application{
    private static int floor = 1;
    private static int room = 1;
    private ArrayList<ArrayList<Room>> floors;
    private Timer clock;
    private JRadioButton heatingJRadioButton;
    private JRadioButton coolingJRadioButton;

    // V2 Components
    private JComboBox floorPicker;
    private JComboBox roomPicker;

    public HVAC() {
        super(640, 480, "HVAC");
        updateTemp();
        updateHeatingCoolingLocked();
    }

    @Override
    public void setup() {
        this.floors = new ArrayList<ArrayList<Room>>();

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
        });

        this.clock.start();

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
//            ///// update room dropdown here

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

public JPanel getBottomControls(){
    JPanel bottomJPanel = new JPanel();

    return bottomJPanel;
}

private JPanel getButtons2() {

    JPanel jPanel = new JPanel();
    jPanel.setBorder(new EmptyBorder(0, 0, 0, 25));
    jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
    jPanel.setAlignmentX(50);


    JButton startStopJButton = new JButton("Start/Stop");
    startStopJButton.addActionListener((e)->{
        if (this.clock.isRunning()){
            this.clock.stop();
        } else {
            this.clock.start();
        }
    }); 

    // jPanel.add(tempPanel, BorderLayout.NORTH);
    jPanel.add(startStopJButton);
    return jPanel;
}

// #endregion Components V2


    private void updateHeatingCoolingLocked(){
        if (room-1 >= floors.get(floor-1).size()){
            this.heatingJRadioButton.setSelected(false);
            this.coolingJRadioButton.setSelected(false);
        } else {
            this.heatingJRadioButton.setSelected(this.floors.get(floor-1).get(room-1).isHeating());
            this.coolingJRadioButton.setSelected(this.floors.get(floor-1).get(room-1).isCooling());
        }
        
    }

    private void updateTemp(){
        
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
    public void draw(Graphics g) {
        this.floors.get(HVAC.floor-1).forEach((r)->{
            r.drawP(g, getWidth(), getHeight());
        });
    }

    public static void main(String args[]) {
        HVAC hvac = new HVAC();
    }

    

}
