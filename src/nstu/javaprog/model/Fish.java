package nstu.javaprog.model;

abstract class Fish implements CanvasElement {
    int x, y;
    int xSpeed, ySpeed;

    Fish(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    final void normalize(int xMax, int yMax, int picWidth, int picHeight) {
        int centredX = x + picWidth / 2;
        if (centredX < picWidth / 2 || centredX > xMax - picWidth / 2) {
            centredX = Math.abs(centredX - picWidth / 2) <
                    Math.abs(centredX - xMax + picWidth / 2)
                    ? picWidth / 2
                    : xMax - picWidth / 2;
            xSpeed = -xSpeed;
        }
        x = centredX - picWidth / 2;

        int centredY = y + picHeight / 2;
        if (centredY < picHeight / 2 || centredY > yMax - picHeight / 2) {
            centredY = Math.abs(centredY - picHeight / 2) <
                    Math.abs(centredY - yMax + picHeight / 2)
                    ? picHeight / 2
                    : yMax - picHeight / 2;
            ySpeed = -ySpeed;
        }
        y = centredY - picHeight / 2;
    }
}