package game.graphics;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import game.Game;
import game.entity.Player;
import game.level.Level;
import game.math.Vec2d;
import kuusisto.tinysound.TinySound;

public class Scene extends GLCanvas implements GLEventListener {

    private GLU glu = new GLU();
    private static long totalUpdates;
    private Player player;
    public Vec2d camera = new Vec2d();

    private int[] lightMapFbo = new int[1];
    private int[] lightMapTexture = new int[1];

    public Scene(int width, int height, GLCapabilities caps) {
        super(caps);
        setSize(width, height);
        totalUpdates = 0;
    }

    public static long getTotalUpdates() {
        return totalUpdates;
    }

    private void update() {
        Game.game.input.update();
        if(Game.game.input.resetDown && !Game.game.input.resetWasDown) {
            Level.getCurrentLevel().restart();
        }

        Level.getCurrentLevel().update();
        player.update();

        camera.x = Math.floor(player.position.x / (double) Game.WIDTH) * Game.WIDTH;
        camera.y = Math.floor(player.position.y / (double) Game.HEIGHT) * Game.HEIGHT;

        totalUpdates++;
    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        Level.getCurrentLevel().render(gl);
   }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glViewport(0, 0, getWidth(), getHeight());
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, getWidth(), 0, getHeight());
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glDisable(GL2.GL_DEPTH_TEST);

        SpriteSheet.loadSpriteSheets();
        Sprite.loadSprites();
        Shader.loadShaders(gl);

        TinySound.init();

        player = (Player) new Player.Builder().position(400, 300)
                .sprite(Sprite.playerIdle[0]).animation(Animation.playerIdle)
                .build();

        FrameBuffer.initFrameBuffers(gl);
        System.out.println(System.getProperty("sun.arch.data.model"));

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        update();
        render(glAutoDrawable);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, getWidth(), getHeight());
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, getWidth(), 0, getHeight());
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public Player getPlayer() {
        return player;
    }

    public Vec2d getCamera() {
        return camera;
    }

    public int[] getLightMapFbo() {
        return lightMapFbo;
    }

    public int[] getLightMapTexture() {
        return lightMapTexture;
    }
}
