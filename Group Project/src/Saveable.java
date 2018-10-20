import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Interface to allow for easy saving of an instance
 * <p>
 * When saving all objects contained by implementing class
 * must also implement this interface
 */
public interface Saveable extends Serializable{

    /**
     * Save this class instance
     * <p>
     * All objects contained in this class must implement Saveable also
     */
    default public void save(){
        ObjectOutputStream objectOutputStream;
        String fileName = this.getClass().getSimpleName() + ".svbl";
        
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
			objectOutputStream.writeObject(this);
			// System.out.println("Saved");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    /**
     * Save this class instance
     * <p>
     * All objects contained in this class must implement Saveable also
     * 
     * @param fileName name of file to save to
     */
    default public void save(String fileName){
        ObjectOutputStream objectOutputStream;
        
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
			objectOutputStream.writeObject(this);
			// System.out.println("Saved");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

}