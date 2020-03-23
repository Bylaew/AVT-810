
public class CarFactory implements AbstractFactory {

    public Vehicle createVehicle(){
        return new Car();
    }

    public Vehicle createVehicle(float x, float y){
        return new Car(x, y);
    }
}
