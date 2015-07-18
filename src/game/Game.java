package game;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import game.graphics.Scene;
import game.input.Input;

import javax.swing.*;

public class Game implements Runnable {

    static {
        GLProfile.initSingleton();
    }

    public static Game game;
    private JFrame frame;

    private boolean isRunning;
    private Thread thread;

    public static final int WIDTH = 800, HEIGHT = 608;
    public static String title = "I Wanna Do the Thing";

    public Input input;

    public static Scene scene;
    public static int fps = 50;

    public Game() {
        frame = new JFrame();

        input = new Input();
        scene.addKeyListener(input);
    }

    public void run() {

    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        scene = new Scene(WIDTH, HEIGHT, caps);

        game = new Game();
        game.frame.setTitle(title);
        game.frame.setSize(WIDTH, HEIGHT);
        game.frame.add(scene);
        game.frame.setResizable(false);
        game.frame.pack();
        game.frame.setVisible(true);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        scene.addGLEventListener(scene);

        final FPSAnimator animator = new FPSAnimator(fps);
        animator.add(scene);
        animator.start();

        game.start();
    }
}
