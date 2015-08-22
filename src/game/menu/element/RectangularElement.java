package game.menu.element;

import com.jogamp.opengl.GL2;
import game.graphics.Color4f;
import game.graphics.RenderUtils;

import java.awt.*;

public class RectangularElement extends Element {

    private Dimension size;
    private Color4f color;

    private int borderThickness;
    private Color4f borderColor;

    private RectangularElement(Builder builder) {
        super(builder);

        size = builder.size;
        color = builder.color;

        borderThickness = builder.borderThickness;
        borderColor = builder.borderColor;
    }

    public void render(GL2 gl) {
        RenderUtils.fillRect(position, size.width, size.height, color, gl);

        if(borderThickness <= 0)
            return;

        // Draw border
        RenderUtils.fillRect(position, size.width, borderThickness, borderColor, gl); // Top
        RenderUtils.fillRect(position.add(0, borderThickness), borderThickness, size.height - 2 * borderThickness,
                borderColor, gl); // Left
        RenderUtils.fillRect(position.add(size.width - borderThickness, borderThickness), borderThickness,
                size.height - 2 * borderThickness, borderColor, gl); // Right
        RenderUtils.fillRect(position.add(0, size.height - borderThickness), size.width, borderThickness, borderColor,
                gl); // Bottom
    }

    public static class Builder extends Element.Builder {
        private Dimension size;
        private Color4f color;

        private int borderThickness;
        private Color4f borderColor;

        public Element build() {
            return new RectangularElement(this);
        }

        public Builder size(Dimension size) {
            this.size = size;
            return this;
        }

        public Builder size(int width, int height) {
            this.size = new Dimension(width, height);
            return this;
        }

        public Builder color(Color4f color) {
            this.color = color;
            return this;
        }

        public Builder color(float r, float g, float b, float a) {
            color = new Color4f(r, g, b, a);
            return this;
        }

        public Builder borderThickness(int thickness) {
            borderThickness = thickness;
            return this;
        }

        public Builder borderColor(Color4f color) {
            borderColor = color;
            return this;
        }

        public Builder borderColor(float r, float g, float b, float a) {
            borderColor = new Color4f(r, g, b, a);
            return this;
        }
    }
}

