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
    float fadeAlpha;

    public void load() {
        music.play(true, 0.0);
        loadTime = Game.screen.getTotalUpdates();
        exitTime = 0;
        state = State.ENTERING;
    }

    protected void init() {
        music = TinySound.loadMusic("/sound/music/menu.ogg");
        Menu.setMenu(Menu.mainMenu);
    }

    public void update() {
        double volume = finalMusicVolume;
        if(state == State.ENTERING) {
            if (music.getVolume() < finalMusicVolume) {
                volume = (Game.screen.getTotalUpdates() - loadTime) / 60d * finalMusicVolume;
            }
            music.setVolume(volume);
            fadeAlpha = (60 - Math.min(60, Game.screen.getTotalUpdates() - loadTime)) / 60f;

            if(Game.screen.getTotalUpdates() - loadTime > 60) {
                music.setVolume(finalMusicVolume);
                fadeAlpha = 0;
                state = State.ACTIVE;
            }
        } else if(state == State.EXITING) {
            if(music.getVolume() > 0)
                volume = (1 - (Game.screen.getTotalUpdates() - exitTime) / 60d) * finalMusicVolume;
            else
                volume = 0;
            music.setVolume(volume);
            fadeAlpha = Math.min(60, Game.screen.getTotalUpdates() - exitTime) / 60f;
        }

        Menu.menuStack.peek().update();
        if(state == State.EXITING && Game.screen.getTotalUpdates() - exitTime > 60) {
            System.out.println(Game.screen.getTotalUpdates() - exitTime + " exited");
            Game.screen.setScene(nextScene);
            music.stop();
        }
    }

    public void render(GL2 gl) {
        Menu.menuStack.peek().render(gl);

        if(state != State.ACTIVE)
            RenderUtils.fillRect(new Vec2d(), Game.screen.getWidth(), Game.screen.getHeight(),
                new Color4f(0, 0, 0, fadeAlpha), gl);
    }

}
