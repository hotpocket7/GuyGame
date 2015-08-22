package game.menu.element;

import com.jogamp.opengl.GL2;
import game.graphics.Color4f;
import game.graphics.RenderUtils;

import java.awt.*;

public class TextElement extends Element {

    private Font font;
    private Color4f color;
    private String text;

    public TextElement(Builder builder) {
        super(builder);
        font = builder.font;
        text = builder.text;
        color = builder.color;
    }

    public void render(GL2 gl) {
        RenderUtils.drawText(text, position, color, font);
    }

    public static class Builder extends Element.Builder {

        private Font font;
        private Color4f color;
        private String text;

        public Element build() {
            return new TextElement(this);
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder color(Color4f color) {
            this.color = color;
            return this;
        }
    }
}
