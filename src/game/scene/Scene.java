package game.scene;

import com.jogamp.opengl.GL2;
import game.Game;
import game.math.Vec2d;

public abstract class Scene  {

    protected static enum State {
        ENTERING, ACTIVE, EXITING;
    }

    protected State state = State.ACTIVE;

    public Vec2d camera = new Vec2d();

    protected long loadTime = 0, exitTime = 0;
    protected Scene nextScene;

    public Scene() {
        init();
    }

    protected abstract void init();
    public abstract void load();
    public abstract void update();
    public abstract void render(GL2 gl);

    public void exitTo(Scene scene) {
        if(state == State.EXITING) return;

        exitTime = Game.screen.getTotalUpdates();
        nextScene = scene;
        state = State.EXITING;
    }

    public void setCamera(Vec2d camera) {
        this.camera.setEqual(camera);
    }
}
