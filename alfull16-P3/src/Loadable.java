import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


/**
 * Interface for loading objects
 */
public interface Loadable{


    /**
     * Loads an instance from file with matching filename:
     * {className}.svbl
     * 
     * @return instance of an object loaded from .svbl file
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     */
    default public Object load() throws IOException, ClassNotFoundException, FileNotFoundException{
        String fileName = this.getClass().getSimpleName() + ".svbl";
        Object output;
        
//        try{
            ObjectInputStream objectInputStream;
			objectInputStream = new ObjectInputStream(new FileInputStream(fileName)) ;
            output = objectInputStream.readObject();

            objectInputStream.close();
            return output;

//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
//        return null;
    }

    /**
     * Loads an instance from file {@code fileName}
     * 
     * @param fileName - name of file to load from
     * @return instance of an object loaded from .svbl file
     */
    static public Object load(String fileName){
        Object output;
        
        try{
            ObjectInputStream objectInputStream;
			objectInputStream = new ObjectInputStream(new FileInputStream(fileName)) ;
            output = objectInputStream.readObject();

            objectInputStream.close();
            return output;

		} catch (Exception ex) {
			ex.printStackTrace();
        }
        
        return null;
    }

}