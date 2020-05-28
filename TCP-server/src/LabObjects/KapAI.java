package LabObjects;

import Environment.Collect;
import Types.Coord;

import javax.swing.*;
import java.util.*;

public class KapAI extends BaseAI {
    private House temp;
    private TreeMap<Integer, Coord> col_pos=new TreeMap();
    private boolean sim = true;

    public KapAI(JFrame frame){
        this.frame = frame;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (sim) {
                    synchronized (thread){
                    if (!workAI) {
                        try {
                            thread.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Не вышло");
                        }
                    }
                }
                move();
                try{
                    Thread.sleep(33);
                }catch (InterruptedException e){System.out.println("Ошибка");}

                }
            }
        });
    }


    public void move(){
                Vector col = Collect.getInstance().get_all();
                Iterator iter = col.iterator();
                col_pos = Collect.getInstance().get_pos();
                int x_fin, y_fin;
                if(col_pos.size()>0){
                while(iter.hasNext()){
                    temp = (House)iter.next();
                    try{
                    if (!temp.goal && temp.getType()=="Kap"){
                    x_fin=(col_pos.get(temp.getID())).getX();
                    y_fin=(col_pos.get(temp.getID())).getY();
                    if (temp.getX()!=x_fin || temp.getY()!=y_fin){
                        double L=Math.sqrt(Math.pow(x_fin-temp.getX(),2)+Math.pow(y_fin-temp.getY(), 2));
                        if (Math.abs(velocity*((x_fin-temp.getX())/L))<Math.abs(x_fin-temp.getX()))
                        temp.setX((int)(temp.getX()+velocity*((x_fin-temp.getX())/L)));
                        else
                            temp.setX(x_fin);
                        if (Math.abs(velocity*((y_fin-temp.getY())/L))<Math.abs(y_fin-temp.getY()))
                        temp.setY((int)(temp.getY()+velocity*((y_fin-temp.getY())/L)));
                        else
                            temp.setY(y_fin);
                    }
                    if (temp.getX()==x_fin && temp.getY()==y_fin)
                        temp.setGoal(true);
                }
                }catch (NullPointerException e){e.printStackTrace();}
                }
                frame.repaint();}
    }

    public void start(){
        thread.start();
    }

    public synchronized void stop(){
        workAI=false;
    }

    public synchronized void resume(){
        synchronized (thread){
        thread.notify();
        workAI=true;
        }
    }


}
