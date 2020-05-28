package Types;

import LabObjects.IBehaviour;

import java.io.Serializable;

public class Coord implements IBehaviour, Serializable {
        protected int x;
        protected int y;
        public Coord(int x, int y){
            this.x=x;
            this.y=y;
        }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
this.x=x;
    }

    @Override
    public void setY(int y) {
this.y=y;
    }
}
