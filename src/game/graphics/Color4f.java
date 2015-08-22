package game.graphics;

public class Color4f {

    public static final Color4f WHITE = new Color4f(1, 1, 1, 1);

    public float r, g, b, a;

    public Color4f() {}

    public Color4f(float red, float green, float blue, float alpha) {
        r = red;
        g = green;
        b = blue;
        a = alpha;
    }

    public void setEqual(float red, float green, float blue, float alpha) {
        r = red;
        g = green;
        b = blue;
        a = alpha;
    }

    public void setEqual(Color4f color) {
        if(color == null) {
            System.err.println("Null color");
            setEqual(1, 1, 1, 1);
            return;
        }
        setEqual(color.r, color.g, color.b, color.a);
    }
}
