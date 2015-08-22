package game.level.block;

import game.collision.PolygonHitbox;
import game.entity.Entity;
import game.entity.Player;
import game.graphics.Sprite;
import game.math.Vec2d;

public class BlockSpike extends Block {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction direction;

    private BlockSpike(Builder builder) {
        super(builder);
        direction = builder.direction;

        Vec2d vert1 = new Vec2d();
        Vec2d vert2 = new Vec2d();
        Vec2d vert3 = new Vec2d();
        switch(direction) {
            case UP:
                vert1.setEqual(getXPos() + width/2, getYPos());
                vert2.setEqual(getXPos(), getYPos() + height);
                vert3.setEqual(getXPos() + width, getYPos() + height);
                break;
            case RIGHT:
                vert1.setEqual(getXPos(), getYPos());
                vert2.setEqual(getXPos() + width, getYPos() + height/2);
                vert3.setEqual(getXPos(), getYPos() + height);
                break;
            case DOWN:
                vert1.setEqual(getXPos(), getYPos());
                vert2.setEqual(getXPos() + width, getYPos());
                vert3.setEqual(getXPos() + width/2, getYPos() + height);
                break;
            case LEFT:
                vert1.setEqual(getXPos() + width, getYPos());
                vert2.setEqual(getXPos() + width, getYPos() + height);
                vert3.setEqual(getXPos(), getYPos() + height/2);
                break;
        }

        polygonHitbox = new PolygonHitbox(vert1, vert2, vert3);
    }

    protected void onCollide(Entity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            player.die();
        }
    }

    public static class Builder extends Block.Builder {

        public Direction direction;

        {
            solid = false;
            collidable = true;
            special = true;
        }

        public Builder() {}

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Block build() {
            return new BlockSpike(this);
        }
    }

}
