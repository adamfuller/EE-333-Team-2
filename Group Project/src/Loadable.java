import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;


public interface Loadable{

    default public Object load(){
        String fileName = this.getClass().getSimpleName() + ".svbl";
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