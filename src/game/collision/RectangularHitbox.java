package game.collision;

import game.math.Vec2d;

import java.awt.geom.Rectangle2D;

public class RectangularHitbox extends Hitbox {

    public Vec2d position = new Vec2d();
    public double width, height;
    public Rectangle2D.Double bounds;


    public RectangularHitbox(Vec2d position, double width, double height) {
        this.position.setEqual(position);
        this.width = width;
        this.height = height;
        bounds = new Rectangle2D.Double(position.x, position.y, width, height);
    }

    public void updateBounds() {
        bounds.x = position.x;
        bounds.y = position.y;
    }

    public PolygonHitbox getPolygonHitbox() {
        return new PolygonHitbox(
                new Vec2d(position.x, position.y),
                new Vec2d(position.x + width, position.y),
                new Vec2d(position.x + width, position.y + height),
                new Vec2d(position.x, position.y + height)
        );
    }

    public boolean collides(RectangularHitbox hitbox) {
        return bounds.intersects(hitbox.bounds);
    }

    public boolean collides(PolygonHitbox hitbox) {
        return hitbox.collides(this);
    }

}
