package game.collision;

import game.entity.Entity;

public abstract class Hitbox {
    public abstract boolean collides(RectangularHitbox hitbox);
    public abstract boolean collides(PolygonHitbox hitbox);
}
