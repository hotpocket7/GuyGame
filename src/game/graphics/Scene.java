package game.graphics;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import game.Game;
import game.entity.Player;
import game.level.Level;
import game.math.Vec2d;

public class Scene extends GLCanvas implements GLEventListener {

    private GLU glu = new GLU();
    private static long totalUpdates;
    private Player player;
    private Vec2d camera = new Vec2d();

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
        player.update(); //Blocks are updated while checking for collisions in this method

        camera.x = Math.floor((int) player.position.x / (double) getWidth()) * getWidth();
        camera.y = Math.floor((int) player.position.y / (double) getHeight()) * getHeight();

        totalUpdates++;
    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glLoadIdentity();
        gl.glTranslated(-camera.x, camera.y, 0);

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        Level.getCurrentLevel().render(gl);

   }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        System.out.println(gl.glGetString(GL2.GL_VERSION));

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

//      player = new Player(new Vec2d(400, 300), Sprite.playerIdle[0]);
        player = (Player) new Player.Builder().position(400, 300)
                .sprite(Sprite.playerIdle[0]).animation(Animation.playerIdle)
                .build();

        gl.glGenTextures(1, lightMapTexture, 0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, lightMapTexture[0]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, 800, 608, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

        gl.glGenFramebuffers(1, lightMapFbo, 0);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, lightMapFbo[0]);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, lightMapTexture[0], 0);
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
