package game.event;

import game.entity.Entity;
import game.math.Vec2d;

public class EntityBounceEvent implements EntityEvent {

    private Vec2d max, min;

    public EntityBounceEvent(Vec2d max, Vec2d min) {
        this.max = max;
        this.min = min;
    }

    public void activate(Entity entity) {
        Vec2d position = entity.getPos();
        Vec2d velocity = entity.velocity;

        if(max.x != min.x && (position.x > max.x || position.x < min.x)) {
            velocity.x *= -1;
            position.x = position.x > max.x ? max.x : min.x;
            entity.updateBounds();
        }
        if(max.y != min.y && (position.y + 32 > max.y || position.y + 32 < min.y)){
            velocity.y *= -1;
            position.y = position.y + 32 > max.y ? max.y - 32: min.y - 32;
            entity.updateBounds();
        }

    }
}
