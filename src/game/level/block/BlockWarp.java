package game.level.block;

import game.entity.Entity;
import game.entity.Player;
import game.level.Level;

public class BlockWarp extends Block {

    public Level destination;

    public BlockWarp(Builder builder) {
        super(builder);
        destination = Level.levels.get(builder.destID);
    }

    public void onCollide(Entity entity) {
        if(entity instanceof Player) {
            Level.setLevel(destination);
            entity.setPos(destination.spawn);
        }
    }

    public static class Builder extends Block.Builder {

        private int destID;

        public Entity build() {
            return new BlockWarp(this);
        }

        public Builder destination(int destID) {
            this.destID = destID;
            return this;
        }
    }
}
