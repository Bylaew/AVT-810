import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public class Singleton implements Serializable
{
    private Singleton(){}

    private static Singleton instance;
    public Vector<House> houseList = new Vector<>(100);
    public HashSet<Integer> idList = new HashSet<>(100);
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
    public void saveObj() throws IOException {
        FileOutputStream outFile =new FileOutputStream("Obj.out");
        ObjectOutputStream outStream = new ObjectOutputStream(outFile);
        outStream.writeObject(this);
    }

}

