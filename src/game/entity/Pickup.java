package game.entity;

import game.event.Event;
import game.graphics.Sprite;
import game.math.Vec2d;

public abstract class Pickup extends Entity {

    protected Pickup(Builder builder) {
        super(builder);
    }

    protected void onCollide(Entity entity) {
        if((entity instanceof Player))
            active = false;
    }

}
