package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Input implements KeyListener {

    public boolean[] keyDown;
    private HashMap<String, Boolean> currentKeysTemp = new HashMap<>();
    private HashMap<String, Boolean> currentKeys = new HashMap<>(), previousKeys = new HashMap<>();

    public Input() {
        keyDown = new boolean[65535];
    }

    public void update() {
        previousKeys = (HashMap<String, Boolean>) currentKeys.clone();
        currentKeys = (HashMap<String, Boolean>) currentKeysTemp.clone();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        keyDown[code] = true;
        handleInput(e, true);
        if(code == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        keyDown[code] = false;
        handleInput(e, false);
    }

    public void keyTyped(KeyEvent e) {

    }

    private void handleInput(KeyEvent e, boolean pressed) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:case KeyEvent.VK_A:
                putKey("LEFT", pressed);
                break;
            case KeyEvent.VK_RIGHT:case KeyEvent.VK_D:
                putKey("RIGHT", pressed);
                break;
            case KeyEvent.VK_SHIFT:
                putKey("JUMP", pressed);
                putKey("SELECT", pressed);
                break;
            case KeyEvent.VK_DOWN:
                putKey("DOWN", pressed);
                break;
            case KeyEvent.VK_UP:
                putKey("UP", pressed);
                break;
            case KeyEvent.VK_R:
                putKey("RESET", pressed);
                break;
            case KeyEvent.VK_S:
                putKey("SAVE", pressed);
                break;
        }
    }

    private void putKey(String key, boolean pressed) {
        currentKeysTemp.put(key, pressed);
    }

    public boolean keyDown(String key) {
        key = key.toUpperCase();
        return currentKeys.getOrDefault(key, false);
    }

    public boolean keyWasDown(String key) {
        key = key.toUpperCase();
        return previousKeys.getOrDefault(key, false);
    }

    public boolean keyHit(String key) {
        return keyDown(key) && !keyWasDown(key);
    }

    public boolean keyReleased(String key) {
        return keyWasDown(key) && !keyDown(key);
    }


}
