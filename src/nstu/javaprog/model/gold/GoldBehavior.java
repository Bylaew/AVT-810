package nstu.javaprog.model.gold;

import nstu.javaprog.model.Behavior;

public final class GoldBehavior extends Behavior {
    public GoldBehavior(int period) {
        super(period);
    }

    @Override
    public void act() {
        habitat.doForEachEntity(entity -> {
            if (entity instanceof Gold)
                entity.move();
        });
    }
}
