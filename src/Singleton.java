import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class Singleton {
    private static Singleton instance;
    public ArrayList<Vehicle> vehicles = new ArrayList<>(1000);
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
            id = (int)(Math.random()*100);
        } while (idSet.contains(id));
        return id;
    }
}