import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface Saveable extends Serializable{

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