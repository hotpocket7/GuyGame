package game.scene;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.Color4f;
import game.graphics.RenderUtils;
import game.graphics.SpriteSheet;
import game.math.Vec2d;
import game.menu.Menu;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;


public class MenuScene extends Scene {

    private Music music;
    private double finalMusicVolume = 0.2;

    private long loadTime;

    public void load() {
        music.play(true, 0.0);
        loadTime = System.currentTimeMillis();
    }

    protected void init() {
        music = TinySound.loadMusic("/sound/music/menu.ogg");
        Menu.setMenu(Menu.mainMenu);
    }

    public void update() {
        if(music.getVolume() < finalMusicVolume) {
            double volume = (System.currentTimeMillis() - loadTime) / 1000d * finalMusicVolume;
            music.setVolume(volume);
        }

        Menu.menuStack.peek().update();
    }

    public void render(GL2 gl) {
        Menu.menuStack.peek().render(gl);

        float alpha = (1000 - Math.min(1000, System.currentTimeMillis() - loadTime)) / 1000f;
        RenderUtils.fillRect(new Vec2d(), Game.screen.getWidth(), Game.screen.getHeight(),
                new Color4f(0, 0, 0, alpha), gl);
    }

}
