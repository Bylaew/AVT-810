package Environment;

import LabObjects.House;
import Types.Coord;
import java.util.*;

public class Collect {
    private Vector<House> container = new Vector();
    private HashSet id_container = new HashSet();
    private TreeMap life_container = new TreeMap();
    private TreeMap pos_obj = new TreeMap();
    private int countWood=0, countKap=0;
    private static volatile Collect instance;

    private Collect(){

    }

    public static class CollHolder{
        public static final Collect Hold_Collect_INSTANCE = new Collect();
    }

    public void add_obj(House obj, long time_life, Habitat frame){
        synchronized(Collect.class){
        container.add(obj);
        life_container.put(obj.getID(), time_life);
        if (obj.getType().equals("Kap")){
            countKap++;
            if (!(obj.getX()<(frame.getWidth()*3/8)-obj.getImage().getWidth()/2 && obj.getY()<frame.getHeight()/2-obj.getImage().getHeight()/2)){
            int x = (int)((Math.random()*(frame.getWidth()*3/8))-obj.getImage().getWidth()/2);
            int y = (int)((Math.random()*frame.getHeight()/2)-obj.getImage().getHeight()/2);
            System.out.println(x);
            System.out.println(y);
            pos_obj.put(obj.getID(), new Coord(x,y));}
            else obj.setGoal(true);
        }
        if (obj.getType().equals("Wood")){
            countWood++;
            if (!(obj.getX()>(frame.getWidth()*3/8)-obj.getImage().getWidth()/2 && obj.getY()>frame.getHeight()/2-obj.getImage().getHeight()/2)){
                int x = (int)(Math.random()*(frame.getWidth()*3/8))+frame.getWidth()*3/8-obj.getImage().getWidth()/2;
                int y = (int)(Math.random()*frame.getHeight()/2)+frame.getHeight()/2-obj.getImage().getHeight()/2;

                pos_obj.put(obj.getID(), new Coord(x,y));}
            else obj.setGoal(true);
        }
    }
    }

    public House get_obj(int index){
       synchronized (Collect.class){
        return container.get(index);
        }
    }

    public int size_obj(){
        synchronized (Collect.class){
        return container.size();
        }
    }

    public void clear(){
        container.clear();
        id_container.clear();
        life_container.clear();
    }

    public boolean add_ID(int ID){
        synchronized (Collect.class){
        return id_container.add(ID);
        }
    }



    public void round(long time, int life_time_wood, int life_time_kap){
        synchronized (Collect.class){
        Iterator it_vec = container.iterator();
        while(it_vec.hasNext()){
            House temp = (House)it_vec.next();
            if(temp.getType()=="Wood" && (int)(time-(long)life_container.get(temp.getID()))>=life_time_wood)
            {
                id_container.remove(temp.getID());
                life_container.remove(temp.getID());
                pos_obj.remove(temp.getID());
                it_vec.remove();
                countWood--;
            }
            else if(temp.getType()=="Kap" && (int)(time-(long)life_container.get(temp.getID()))>=life_time_kap)
            {
                id_container.remove(temp.getID());
                life_container.remove(temp.getID());
                pos_obj.remove(temp.getID());
                it_vec.remove();
                countKap--;
            }
        }
        }
    }

    public TreeMap comeback (){
        return life_container;
    }

    public Vector get_all(){
        return (Vector)container.clone();
    }

    public TreeMap get_pos(){
        return (TreeMap)pos_obj.clone();
    }

    public static Collect getInstance(){
        if (instance==null){
            synchronized (Collect.class){
                if (instance==null)
                    instance=new Collect();
            }
        }
        return instance;
    }

    public int Wood_count(){
        return countWood;
    }

    public int Kap_count(){
        return countKap;
    }



}

