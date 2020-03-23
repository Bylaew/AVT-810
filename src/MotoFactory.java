public class MotoFactory implements  AbstractFactory{

    public Vehicle createVehicle(){
        return new Moto();
    }

    public Vehicle createVehicle(float x, float y){
        return new Moto(x, y);
    }
}
