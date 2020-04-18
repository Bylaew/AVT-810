import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public class Singleton
{
    private Singleton(){}

    private static Singleton instance;
    public Vector<House> houseList = new Vector<>(1000);
    public HashSet<Integer> idList = new HashSet<>(1000);
    public TreeMap<Integer, Long> mapList = new TreeMap<>();
    public static Singleton getInstance()
    {
        if(instance == null)
            instance = new Singleton();
        return instance;
    }
    public int idRandom()
    {
        int id;
        do {
            id = (int)(Math.random()*100);
        } while (idList.contains(id));
        return id;

    }
    public void clear()
    {
        houseList.clear();
        idList.clear();
        mapList.clear();
    }

}

