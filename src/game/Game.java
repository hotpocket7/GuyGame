package game;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import game.input.Input;

import javax.swing.*;
import java.awt.*;

public class Game {

    static {
        GLProfile.initSingleton();
    }

    public static Game game;
    private JFrame frame;

    public static final int WIDTH = 800, HEIGHT = 608;
    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
    public static String title = "I Wanna Do the Thing";

    public Input input;

    public static Screen screen;
    public static int fps = 50;

    public Game() {
        frame = new JFrame();

        input = new Input();
        screen.addKeyListener(input);
    }

    public static void main(String[] args) {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        screen = new Screen(SIZE, HEIGHT, caps);

        game = new Game();
        game.frame.setTitle(title);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.frame.add(screen);
        game.frame.setResizable(false);
        game.frame.pack();

        game.frame.setVisible(true);
        game.frame.setLocationRelativeTo(null);
        screen.addGLEventListener(screen);

        final FPSAnimator animator = new FPSAnimator(fps);
        animator.add(screen);
        animator.start();
    }
}
