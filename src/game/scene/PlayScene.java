package game.scene;

import com.jogamp.opengl.GL2;
import game.Game;
import game.entity.Player;
import game.graphics.Animation;
import game.graphics.Color4f;
import game.graphics.RenderUtils;
import game.graphics.Sprite;
import game.level.Level;
import game.math.Vec2d;
import game.sound.Song;
import kuusisto.tinysound.Music;

public class PlayScene extends Scene {

    private Player player;
    private float fadeAlpha = 1;

    public void load() {
        if(Level.getCurrentLevel() != null && Level.getCurrentLevel().song != null) {
            Level.getCurrentLevel().song.play(true, 0);
        }
        loadTime = Game.screen.getTotalUpdates();
        state = State.ENTERING;
    }

    protected void init() {
        player = (Player) new Player.Builder().position(400, 300)
                .sprite(Sprite.playerIdle[0]).animation(Animation.playerIdle)
                .build();
        camera = new Vec2d();
    }

    public void update() {
        Song song = Level.getCurrentLevel().song;
        double finalMusicVolume = Level.getCurrentLevel().song.getMaxVolume();
        double volume = finalMusicVolume;

        if(state == State.ENTERING) {
            if (song.getVolume() < finalMusicVolume) {
                volume = (Game.screen.getTotalUpdates() - loadTime) / 60f * finalMusicVolume;
            }
            song.setVolume(volume);
            fadeAlpha = (60 - Math.min(60, Game.screen.getTotalUpdates() - loadTime)) / 60f;

            if(Game.screen.getTotalUpdates() - loadTime > 60) {
                song.setVolume(finalMusicVolume);
                fadeAlpha = 0;
                state = State.ACTIVE;
            }
        } else if(state == State.EXITING) {
            if(song.getVolume() > 0)
                volume = (1 - (Game.screen.getTotalUpdates() - exitTime) / 60d) * finalMusicVolume;
            else
                volume = 0;
            song.setVolume(volume);
            fadeAlpha = Math.min(60, Game.screen.getTotalUpdates() - exitTime) / 60f;
        }

        if (Game.game.input.keyHit("reset")) {
            Level.getCurrentLevel().restart();
        }

        Level.getCurrentLevel().update();
        player.update();

        camera.x = Math.floor(player.getXPos() / (double) Game.WIDTH) * Game.WIDTH;
        camera.y = Math.floor(player.getYPos() / (double) Game.HEIGHT) * Game.HEIGHT;
    }

    public void render(GL2 gl) {
        Level.getCurrentLevel().render(gl);

        if(state != State.ACTIVE)
            RenderUtils.fillRect(new Vec2d(), Game.screen.getWidth(), Game.screen.getHeight(),
                new Color4f(0, 0, 0, fadeAlpha), gl);
    }

    public Player getPlayer() {
        return player;
    }

}
