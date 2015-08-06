package game.graphics.light;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.Color4f;
import game.math.Vec2d;
import static java.lang.Math.*;

public class RadialLight extends Light {

    public float xRadius, yRadius;

    public RadialLight(Vec2d position, float radius, Color4f color, Color4f ambientColor) {
        this.position.setEqual(position);
        xRadius = radius;
        yRadius = radius;
        this.color.setEqual(color);
        this.ambientColor.setEqual(ambientColor);
    }

    public RadialLight(Vec2d position, float xRadius, float yRadius, Color4f color, Color4f ambientColor) {
        this.position.setEqual(position);
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        this.color.setEqual(color);
        this.ambientColor.setEqual(ambientColor);
    }

    public void render(GL2 gl) {
        int nsd = 16;
        Vec2d camera = Game.scene.getCamera();

        if(position.x < camera.x || position.x > camera.x + Game.WIDTH
                || position.y < camera.y || position.y > camera.y + Game.HEIGHT)
            return;

        float x = (float) (position.x - camera.x);
        float y = (float) (position.y - camera.y);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor4f(color.r, color.g, color.b, color.a);
        gl.glVertex2f(x, y);

        gl.glColor4f(ambientColor.r, ambientColor.g, ambientColor.b, 0);
        for(float angle = 0; angle <= 2 * PI + 2 * PI / nsd; angle += 2 * PI / nsd) {
            gl.glVertex2f(
                    x + xRadius * (float) cos(angle),
                    y + yRadius * (float) sin(angle)
            );
        }
        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);
    }
}
