package game.menu.element;

import com.jogamp.opengl.GL2;
import game.event.Event;

public abstract class SelectableElement extends Element {

    private Element element;
    private SelectableElement up, down, left, right;

    public SelectableElement(Builder builder) {
        super(builder);
        element = builder.element;
        position = element.position;
        this.up = builder.up;
        this.down = builder.down;
        this.left = builder.left;
        this.right = builder.right;
    }

    public abstract void select();

    public void render(GL2 gl) {
        element.render(gl);
    }

    public SelectableElement down() {
        return down;
    }

    public SelectableElement up() {
        return up;
    }

    public SelectableElement left() {
        return left;
    }

    public SelectableElement right() {
        return right;
    }

    public void setDown(SelectableElement down) {
        this.down = down;
    }

    public void setUp(SelectableElement up) {
        this.up = up;
    }

    public void setLeft(SelectableElement left) {
        this.left = left;
    }

    public void setRight(SelectableElement right) {
        this.right = right;
    }

    public static class Builder extends Element.Builder {

        private Element element;
        private Event onSelect;
        private SelectableElement up, down, left, right;

        public Element build() {
            return new SelectableElement(this) {
                public void select() {
                    onSelect.activate();
                }
            };
        }

        public Builder onSelect(Event event) {
            onSelect = event;
            return this;
        }

        public Builder element(Element element) {
            this.element = element;
            return this;
        }

        public Builder up(SelectableElement element) {
            this.up = up;
            return this;
        }

        public Builder down(SelectableElement element) {
            this.down = down;
            return this;
        }

        public Builder left(SelectableElement element) {
            this.left = left;
            return this;
        }

        public Builder right(SelectableElement element) {
            this.right = right;
            return this;
        }
    }
}
