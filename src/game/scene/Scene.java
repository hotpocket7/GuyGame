package game.scene;

import com.jogamp.opengl.GL2;
import game.math.Vec2d;

public abstract class Scene  {

    public Vec2d camera = new Vec2d();

    public Scene() {
        init();
    }

    protected abstract void init();
    public abstract void load();
    public abstract void update();
    public abstract void render(GL2 gl);

    public void setCamera(Vec2d camera) {
        this.camera.setEqual(camera);
    }
}
