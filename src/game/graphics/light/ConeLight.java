package game.graphics.light;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.Color4f;
import game.math.Vec2d;
import static java.lang.Math.*;

public class ConeLight extends Light {

    public float radius;
    public float startAngle, endAngle; //Radians

    public ConeLight(Vec2d position, float radius, float startAngle, float endAngle, Color4f color, Color4f ambientColor) {
        this.position.setEqual(position);
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.color.setEqual(color);
        this.ambientColor.setEqual(ambientColor);
    }

    public void render(GL2 gl) {
        int nsd = 16;
        float delta = (endAngle - startAngle) / nsd;

        Vec2d camera = Game.game.scene.getCamera();

        if(position.x < camera.x || position.x > camera.x + Game.scene.getWidth()
                || position.y < camera.y || position.y > camera.y + Game.scene.getHeight())
            return;

        float x = (float) (position.x - camera.x);
        float y = (float) (position.y - camera.y);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor4f(color.r, color.g, color.b, color.a);
        gl.glVertex2f(x, y);

        gl.glColor4f(ambientColor.r, ambientColor.g, ambientColor.b, 0);
        for(float angle = startAngle; angle <= endAngle + delta; angle += delta) {
            gl.glVertex2f(x + radius * (float) cos(angle),
                          y + radius * (float) sin(angle));
        }
        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);
    }

}
