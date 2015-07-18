package game.event;

import game.entity.Player;
import game.level.block.Block;

public final class Events {
    //Entity events
    public static final EntityEvent destroyOnLeaveScreen = entity -> {
        if(!entity.isOnScreen())
            entity.destroy();
    };

    //Collision events
    public static final CollisionEvent destroyOnTouchBlock = (collider, collided) -> {
        if(collided instanceof Block)
            collider.destroy();
    };

    public static final CollisionEvent killPlayerOnTouch = (collider, collided) -> {
        if(collided instanceof Player) {
            Player player = (Player) collided;
            player.die();
        }
    };
}
