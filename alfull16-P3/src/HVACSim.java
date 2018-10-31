/* TODO
    Add ability to save each to a specific output file
    Add ability to load from a specific file
    
*/
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alfuller
 */
public class HVACSim {
    private Logger logger = new PrintLogger();
    private Heater heater = new Heater(logger);
    private TempSensor tempSensor = new TempSensor(logger);
    private Controller controller = new Controller(logger);
    private Clock clock = new Clock(logger);
    private Blower blower = new Blower(logger);
    private Room room = new Room();
    private int timesToRun = 1;
    private boolean shouldOutput = false;
    private boolean shouldSave = false;
    private String outputFileName = null;
    
    /**
     * 
     */
    public HVACSim(){
        
    }
    
    /**
     * 
     */
    public void setup(){
        this.logger = new StringLogger();
        this.heater = new Heater(logger);
        this.tempSensor = new TempSensor(logger);
        this.controller = new Controller(logger);
        this.clock = new Clock(logger);
        this.blower = new Blower(logger);
        this.room = new Room();
        
        this.heater.setState(false);
        
        this.room.add(this.heater);
        this.room.add(this.tempSensor);
        this.room.add(blower);
        
        this.controller.connect(tempSensor);    // connect temperature sensor
        this.controller.connect(heater);     // connect heater
        this.controller.connect(blower);
        
        this.clock.add(this.controller);
        this.clock.add(room);
        
    }
    
    /**
     * 
     * @param args 
     */
    public void parseInputs(String args[]){
        for (int i = 0; i<args.length; i++){
            if (args[i].equals("-t") && i != args.length-1){
                this.room.setTemp(Double.parseDouble(args[i+1]));
            }else if (args[i].equals("-d") && i != args.length-1){
                this.parseDisturbanceArray(args[i+1]);
            } else if (args[i].equals("-r") && i != args.length-1){
                this.timesToRun = Integer.parseInt(args[i+1]);
            } else if (args[i].equals("-o")){
                this.shouldOutput = true;
            }
        }
    }
    
    /**
     * Pulls the disturbance array out from the input string
     * @param distString_ string containing comma separated disturbance array
     */
    private void parseDisturbanceArray(String distString_){
        // remove anything that isn't a number or comma then split it by commas
                String distStrings[] = distString_.replaceAll("[^\\d.,]", "").split(",");
                
                ArrayList<Double> distArrayList = new ArrayList<>();
                for(String distString: distStrings){
                    distArrayList.add(Double.parseDouble(distString));
                }
                double distArray[] = new double[distArrayList.size()];
                for (int j = 0; j<distArrayList.size(); j++){
                    distArray[j] = distArrayList.get(j);
                }
                this.room.setDisturbance(distArray);
    }
    
    public void run(){
        for (int i = 0; i<this.timesToRun; i++){
            try{
                this.clock.run();
            } catch(Exception e){
                System.out.println("Clock failed to run");
            }
        }
        
        if (this.shouldOutput){
            System.out.println("RoomTemp: " + this.room.getTemp());
            System.out.println("BlowerState: " + this.blower.getState());
            System.out.println("HeaterState: " + this.heater.getState());
        }
    }
    
    public static void main(String args[]){
        System.out.println("HVACSim Name: Adam Fuller BlazerId: alfull16");
        
        HVACSim hvacSim = new HVACSim();
        hvacSim.setup();
        hvacSim.parseInputs(args);
        hvacSim.run();
        
    }
}
