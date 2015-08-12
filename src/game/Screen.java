package game;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import game.entity.Player;
import game.math.Vec2d;
import game.scene.MenuScene;
import game.scene.PlayScene;
import game.scene.Scene;
import game.graphics.*;
import kuusisto.tinysound.TinySound;

import java.awt.*;

public class Screen extends GLCanvas implements GLEventListener {

    private GLU glu = new GLU();
    private static long totalUpdates;
    private Player player;
    private Vec2d camera;

    private Scene currentScene;
    private PlayScene playScene;
    private MenuScene menuScene;

    public Screen(Dimension dimension, int height, GLCapabilities caps) {
        super(caps);
        setSize(dimension);
        totalUpdates = 0;
    }

    public static long getTotalUpdates() {
        return totalUpdates;
    }

    private void update() {
        Game.game.input.update();
        currentScene.update();

        totalUpdates++;
    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        currentScene.render(gl);

//      Level.getCurrentLevel().render(gl);
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

        System.out.println(gl.glGetString(GL2.GL_VENDOR));

        SpriteSheet.loadSpriteSheets();
        Sprite.loadSprites();
        Shader.loadShaders(gl);

        TinySound.init();

        playScene = new PlayScene();
        menuScene = new MenuScene();
        setScene(menuScene);

        FrameBuffer.initFrameBuffers(gl);

    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {
        update();
        render(glAutoDrawable);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, width, 0, height);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public Player getPlayer() {
        return playScene.getPlayer();
    }

    public Scene getScene() {
        return currentScene;
    }

    public void setScene(Scene scene) {
        currentScene = scene;
        currentScene.load();
    }

    public Vec2d getCamera() {
        return currentScene.camera;
    }

    public void setCamera(Vec2d camera) {
        currentScene.setCamera(camera);
    }
}
