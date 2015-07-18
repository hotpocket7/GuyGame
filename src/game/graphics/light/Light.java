package game.graphics.light;

import com.jogamp.opengl.GL2;
import game.graphics.Color4f;
import game.math.Vec2d;

public abstract class Light {
    public Vec2d position = new Vec2d();
    public Color4f color = new Color4f(1, 1, 1, 1);
    public Color4f ambientColor = new Color4f(0, 0, 0, 0);

    public abstract void render(GL2 gl);
}