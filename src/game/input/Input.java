package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {

    public boolean[] keyDown;

    public boolean leftDown, rightDown, jumpDown;
    public boolean jumpWasDown;
    public boolean saveDown, saveWasDown;
    public boolean resetDown, resetWasDown;

    public Input() {
        keyDown = new boolean[65535];
    }

    public void update() {
        saveWasDown = saveDown;
        jumpWasDown = jumpDown;
        resetWasDown = resetDown;

        leftDown = keyDown[KeyEvent.VK_LEFT] || keyDown[KeyEvent.VK_A];
        rightDown = keyDown[KeyEvent.VK_RIGHT] || keyDown[KeyEvent.VK_D];
        jumpDown = keyDown[KeyEvent.VK_SHIFT];
        saveDown = keyDown[KeyEvent.VK_S];
        resetDown = keyDown[KeyEvent.VK_R];
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        keyDown[code] = true;
        if(code == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        keyDown[code] = false;
    }

    public void keyTyped(KeyEvent e) {

    }

}
