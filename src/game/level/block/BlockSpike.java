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
                vert1.setEqual(position.x + width/2, position.y);
                vert2.setEqual(position.x+1, position.y + height);
                vert3.setEqual(position.x + width-1, position.y + height);
                break;
            case RIGHT:
                vert1.setEqual(position.x, position.y + 1);
                vert2.setEqual(position.x + width, position.y + height/2);
                vert3.setEqual(position.x, position.y + height-1);
                break;
            case DOWN:
                vert1.setEqual(position.x+1, position.y);
                vert2.setEqual(position.x + width-1, position.y);
                vert3.setEqual(position.x + width/2, position.y + height);
                break;
            case LEFT:
                vert1.setEqual(position.x + width, position.y + 1);
                vert2.setEqual(position.x + width, position.y + height-1);
                vert3.setEqual(position.x, position.y + height/2);
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
            solid = true;
            collidable = true;
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
