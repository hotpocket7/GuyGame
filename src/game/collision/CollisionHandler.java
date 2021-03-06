package game.collision;

import game.entity.Entity;
import game.entity.Player;
import game.level.block.Block;

import java.awt.geom.Rectangle2D;

public final class CollisionHandler {

    /**
     * Resolves collisions on the x-axis between an entity and a solid rectangular block
     */
    public static void resolveCollisionX(Entity entity, Block block) {
        if(!block.solid)
            return;

        RectangularHitbox entityHitbox = entity.getHitbox();
        RectangularHitbox blockHitbox  = block.getHitbox();

        if(entityHitbox.position.x < blockHitbox.position.x && block.solidLeft) {
            // Collision to right of entity
            entity.setXPos(blockHitbox.position.x - entity.width);
        } else if(block.solidRight) {
            // Collision to left of entity
            entity.setXPos(blockHitbox.position.x + blockHitbox.width);
        } else return;

        entity.updateBounds();
    }

    /**
     * Resolves collisions on the y-axis between an entity and a solid rectangular block
     */
    public static void resolveCollisionY(Entity entity, Block block) {
        if(!block.solid)
            return;

        RectangularHitbox entityHitbox = entity.getHitbox();
        RectangularHitbox blockHitbox  = block.getHitbox();

        if(entityHitbox.position.y > blockHitbox.position.y && block.solidBottom) {
            // Collision above entity
            entity.setYPos(blockHitbox.position.y + blockHitbox.height);
            entity.velocity.y = 0;
        } else if(block.solidTop
                && entity.getYPos() + entity.height -1- (entity.velocity.y - entity.acceleration.y)
                   < block.getYPos() - (block.velocity.y - block.acceleration.y)) {
            // Collision below entity
            entity.setYPos(blockHitbox.position.y - entity.height);
            entity.addToPos(block.velocity);
            entity.velocity.y = 0;
            if(entity instanceof Player) {
                ((Player) entity).state = Player.State.GROUNDED;
            }
        }
        entity.updateBounds();
    }
}

