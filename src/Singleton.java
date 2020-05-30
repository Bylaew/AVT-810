import java.io.*;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public class Singleton implements Serializable {
    private static Singleton instance;
    public  Vector<Vehicle> vehicles = new Vector<>(1000);
    public HashSet<Integer> idSet = new HashSet<>(1000);
    public TreeMap<Integer, Long> Map = new TreeMap<>();

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();
        return instance;
    }


    public void reset() {
        vehicles.clear();
        idSet.clear();
        Map.clear();
    }
    public int generateId(){
        int id;
        do {
            id = (int)(Math.random()*1000);
        } while (idSet.contains(id));
        return id;
    }

    public void saveObj(File file) throws IOException {
        FileOutputStream outFile =new FileOutputStream(file);
        ObjectOutputStream outStream = new ObjectOutputStream(outFile);
        outStream.writeObject(this);
    }
}