/* TODO
    
    
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class HVACSim implements Saveable, Loadable{
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
            } else if (args[i].equals("-s")){
                this.shouldSave = true;
                if (i+1 < args.length && !args[i+1].contains("-")){
                    this.outputFileName = args[i+1];
                    System.out.println("Output filename specified");
                }
            } else if (args[i].equals("-l")){
                String inputFileName = null;
                if (i+1 < args.length && !args[i+1].contains("-")){
                    inputFileName = args[i+1];
                }
                try{
                    HVACSim hvs;
                    // if the filename is provided load from it
                    if (inputFileName == null){
                        hvs = (HVACSim) this.load();
                    } else {
                        hvs = (HVACSim) Loadable.load(inputFileName);
                    }
                    
                    this.blower = hvs.blower;
                    this.clock = hvs.clock;
                    this.controller = hvs.controller;
                    this.room = hvs.room;
                } catch (Exception e){
                    System.out.println("Failed to load from file");
                    this.timesToRun = 0;
                    this.shouldSave = false;
                    break;
                }
                
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
                System.out.println("System failed to run");
                if (System.getProperty("os.name").equals("Mac OS X")){
                    try{
                        double m = Math.random();
                        if (m > 0.7){
                            execute("say " + e.toString());
                        } else if (m < 0.3){
                            execute("say Something went wrong");
                        } else if (m < 0.5) {
                            execute("say Please enter proper parameters");
                        } else {
                            execute("say Maybe try not messing it up");
                        }
                    } catch (Exception e2){

                    }
                }
                e.printStackTrace();
            }
        }
        
        if (this.shouldOutput){
            System.out.println("RoomTemp: " + this.room.getTemp());
            System.out.println("BlowerState: " + this.blower.getState());
            System.out.println("HeaterState: " + this.heater.getState());
        }
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

    public boolean hasOutputFileName(){
        return this.outputFileName!=null;
    }
    
    public String getOutputFileName(){
        return this.outputFileName;
    }
    
    public boolean shouldSave(){
        return this.shouldSave;
    }
    
    public static void main(String args[]){
        System.out.println("HVACSim Name: Adam Fuller BlazerId: alfull16");
        
        HVACSim hvacSim = new HVACSim();
        hvacSim.setup();
        hvacSim.parseInputs(args);
        hvacSim.run();
        
        if (hvacSim.shouldSave() && hvacSim.hasOutputFileName()){
            hvacSim.save(hvacSim.getOutputFileName());
        } else if (hvacSim.shouldSave()) {
            hvacSim.save();
        }
        
        
    }
}
