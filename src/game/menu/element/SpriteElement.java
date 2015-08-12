package game.menu.element;

import com.jogamp.opengl.GL2;
import game.graphics.Sprite;

public class SpriteElement extends Element {

    private Sprite sprite;

    private SpriteElement(Builder builder) {
        super(builder);
        this.sprite = builder.sprite;
    }

    public void render(GL2 gl) {
        sprite.render(position, false, false, gl);
    }

    public static class Builder extends Element.Builder {

        private Sprite sprite;

        public Element build() {
            return new SpriteElement(this);
        }

        public Builder sprite(Sprite sprite) {
            this.sprite = sprite;
            return this;
        }
    }
}
