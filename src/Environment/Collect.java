package Environment;

import LabObjects.House;

import java.util.*;

public class Collect {
    private Vector<House> container = new Vector();
    private HashSet id_container = new HashSet();
    private TreeMap life_container = new TreeMap();
    private Collect(){

    }

    public static class CollHolder{
        public static final Collect Hold_Collect_INSTANCE = new Collect();
    }

    public void add_obj(House obj, long time_life){
        container.add(obj);
        life_container.put(obj.getID(), time_life);
    }

    public House get_obj(int index){
        return container.get(index);
    }

    public int size_obj(){
        return container.size();
    }

    public void clear(){
        container.clear();
        id_container.clear();
        life_container.clear();
    }

    public boolean add_ID(int ID){
        return id_container.add(ID);
    }



    public void round(long time, int life_time_wood, int life_time_kap){
        Iterator it_vec = container.iterator();
        while(it_vec.hasNext()){
            House temp = (House)it_vec.next();
            if(temp.getType()=="Wood" && (int)(time-(long)life_container.get(temp.getID()))==life_time_wood)
            {
                id_container.remove(temp.getID());
                life_container.remove(temp.getID());
                it_vec.remove();
            }
            else if(temp.getType()=="Kap" && (int)(time-(long)life_container.get(temp.getID()))==life_time_kap)
            {
                id_container.remove(temp.getID());
                life_container.remove(temp.getID());
                it_vec.remove();
            }
        }
    }

    public TreeMap comeback (){
        return life_container;
    }

    public static Collect getInstance(){return CollHolder.Hold_Collect_INSTANCE;}
}
