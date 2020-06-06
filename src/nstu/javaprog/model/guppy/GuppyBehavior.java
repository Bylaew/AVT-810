package nstu.javaprog.model.guppy;

import nstu.javaprog.model.Behavior;

public final class GuppyBehavior extends Behavior {
    public GuppyBehavior(int period) {
        super(period);
    }

    @Override
    public void act() {
        habitat.doForEachEntity(entity -> {
            if (entity instanceof Guppy)
                entity.move();
        });
        view.drawEntities();
    }
}
