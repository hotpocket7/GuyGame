package game.entity;

public class JumpPickup extends Pickup {

    public static final int DEFAULT_WIDTH = 18, DEFAULT_HEIGHT = 18;

    private JumpPickup(Builder builder) {
        super(builder);
    }

    protected void onCollide(Entity entity) {
        super.onCollide(entity);
        if(entity instanceof Player) {
            Player player = (Player) entity;
            player.jumpsUsed = player.maxJumps - 1;
        }
    }

    public static class Builder extends Entity.Builder {
        {
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
            collidable = true;
        }
        public Entity build() {
            return new JumpPickup(this);
        }
    }
}
