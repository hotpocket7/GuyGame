package game.level.block;

import game.graphics.Sprite;
import game.math.Vec2d;

public class BlockGeneric extends Block {

    private BlockGeneric(Builder builder) {
        super(builder);
    }

    public static class Builder extends Block.Builder {
        public Builder() {}
        public Block build() {
            return new BlockGeneric(this);
        }
    }
}
