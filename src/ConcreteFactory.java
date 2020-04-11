
public class ConcreteFactory implements AbstractFactory {


    public Car createCar(float x, float y, int id){
        return new Car(x, y, id);
    }

    public Moto createMoto(float x, float y, int id){
        return new Moto(x, y, id);
    }
}
