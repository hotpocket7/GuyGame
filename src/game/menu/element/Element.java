package game.menu.element;

import com.jogamp.opengl.GL2;
import game.math.Vec2d;

public abstract class Element {

    public Vec2d position;

    protected Element(Builder builder) {
        this.position = new Vec2d(builder.position);
    }

    public abstract void render(GL2 gl);

    public static abstract class Builder {

        private Vec2d position = new Vec2d();

        public abstract Element build();

        public Builder position(Vec2d position) {
            this.position.setEqual(position);
            return this;
        }

        public Builder position(double x, double y) {
            position.setEqual(x, y);
            return this;
        }
    }
}
