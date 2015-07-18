package game.event;

import game.entity.Entity;

public interface CollisionEvent {
    public void collide(Entity collider, Entity collidable);
}
