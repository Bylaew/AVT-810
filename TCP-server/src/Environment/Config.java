package Environment;

import java.io.Serializable;

public class Config implements Serializable {
    private transient Habitat h;
    protected int N1, N2, life_time_wood, life_time_kap, woodAI_prior, kapAI_prior;
    protected double P1, P2;
    protected boolean view_info, view_time, woodAI_work, kapAI_work;

    private Config(){
    }



    private static class ConfigHolder{
        public static final Config Hold_Config_INSTANCE = new Config();
    }

    public void setH(Habitat h){
        this.h=h;
        N1=h.N1;
        N2=h.N2;
        life_time_wood=h.life_time_wood;
        life_time_kap=h.life_time_kap;
        P1=h.P1;
        P2=h.P2;
        view_info=h.info_view;
        view_time=h.view_time;
        woodAI_work=h.woodAI_work;
        kapAI_work=h.kapAI_work;
        woodAI_prior=h.wood_prior;
        kapAI_prior=h.kap_prior;
    }

    public Object readResolve(){
        getInstance().N1=this.N1;
        getInstance().N2=this.N2;
        getInstance().life_time_wood=this.life_time_wood;
        getInstance().life_time_kap=this.life_time_kap;
        getInstance().P1=this.P1;
        getInstance().P2=this.P2;
        getInstance().view_info=this.view_info;
        getInstance().view_time=this.view_time;
        getInstance().woodAI_work=this.woodAI_work;
        getInstance().kapAI_work=this.kapAI_work;
        getInstance().woodAI_prior=this.woodAI_prior;
        getInstance().kapAI_prior=this.kapAI_prior;
        return getInstance();
    }


    public static Config getInstance(){
        return ConfigHolder.Hold_Config_INSTANCE;
    }
}
