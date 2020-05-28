package Environment;

import JDBC.HouseBD;
import LabObjects.House;
import Types.Coord;

import java.io.*;
import java.util.*;

public class Collect implements Serializable {
    private Vector<House> container;
    private HashSet id_container;
    private TreeMap life_container;
    private TreeMap pos_obj;
    private int countWood, countKap;
    private static final long serialVersionUID=1L;


    private Collect(){
        container=new Vector<>();
        id_container= new HashSet();
        life_container= new TreeMap();
        pos_obj= new TreeMap();
        countWood=0;
        countKap=0;
    }

    public static class CollHolder{
        public static final Collect Hold_Collect_INSTANCE = new Collect();
    }

    public void addAll_to_DB(){
        HouseBD.getInstance().insert((Vector)container.clone());
    }

    public void save_wood_to_DB(){
        synchronized (Collect.class){
            Vector <House> temp = new Vector<>();
        for (int i=0; i<container.size();i++)
            if (container.get(i).getType().equals("Wood"))
                temp.add(container.get(i));
            HouseBD.getInstance().insert(temp);
        }
    }

    public void save_kap_to_DB(){
        synchronized (Collect.class){
            Vector <House> temp = new Vector<>();
            for (int i=0; i<container.size();i++)
                if (container.get(i).getType().equals("Kap"))
                    temp.add(container.get(i));
            HouseBD.getInstance().insert(temp);
        }
    }


    public void add_obj(House obj, long time_life, Habitat frame){
        synchronized(Collect.class){
            int temp_ID;
            do{
                temp_ID = (int)(Math.random()*10000);
            }while(!add_ID(temp_ID));
            obj.setID(temp_ID);
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
        pos_obj.clear();
        countWood=0;
        countKap=0;
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
            if (life_container.get(temp.getID())==null){
                life_container.put(temp.getID(), time);
            }
            else{
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

    public static Collect getInstance() {
        return CollHolder.Hold_Collect_INSTANCE;
    }

    public int Wood_count(){
        return countWood;
    }

    public int Kap_count(){
        return countKap;
    }

    public Object readResolve() {
        getInstance().clear();
        getInstance().container=this.container;
        getInstance().life_container=this.life_container;
        Collection live = life_container.entrySet();
        Iterator it = live.iterator();
        while(it.hasNext()){
            Map.Entry me= (Map.Entry)it.next();
            me.setValue(null);
        }
        getInstance().pos_obj=this.pos_obj;
        getInstance().id_container=this.id_container;
        getInstance().countKap=this.countKap;
        getInstance().countWood=this.countWood;
        return getInstance();
    }






}

