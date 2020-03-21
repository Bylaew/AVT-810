package nstu.javaprog.lab1.view.element;

public abstract class Fish implements CanvasElement {
    protected int x, y;
    protected int xSpeed, ySpeed;

    protected Fish(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
}