public abstract class BaseAI extends Thread{
    boolean working;
    @Override
    public synchronized void start() {
        super.start();
        working = true;
    }

}
