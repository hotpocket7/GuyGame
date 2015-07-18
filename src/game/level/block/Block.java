package game.level.block;

import com.jogamp.opengl.GL2;
import game.entity.Entity;
import game.graphics.Sprite;
import game.math.Vec2d;

public abstract class Block extends Entity {

    public boolean solid = true;
    public boolean solidTop = true, solidBottom = true, solidLeft = true, solidRight = true;

    protected Block(Builder builder) {
        super(builder);
        solidTop = builder.solidTop;
        solidBottom = builder.solidBottom;
        solidLeft = builder.solidLeft;
        solidRight = builder.solidRight;
        solid = builder.solid;
    }

    public void render(GL2 gl) {
        super.render(false, false, gl);
    }

    public boolean hasSpecialHitbox() {
        return polygonHitbox != null;
    }

    public static abstract class Builder extends Entity.Builder {

        {
            width = 32;
            height = 32;
        }

        public boolean solid = true;
        public boolean solidTop = true, solidBottom = true, solidLeft = true, solidRight = true;

        public Builder solidTop(boolean b) {
            solidTop = b;
            return this;
        }

        public Builder solidBottom(boolean b) {
            solidBottom = b;
            return this;
        }

        public Builder solidLeft(boolean b) {
            solidLeft = b;
            return this;
        }

        public Builder solidRight(boolean b) {
            solidRight = b;
            return this;
        }

        public Builder solid(boolean b) {
            solid = b;
            return this;
        }
    }
}
