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

public class PlayScene extends Scene {

    private Player player;

    public void load() {
        if(Level.getCurrentLevel() != null && Level.getCurrentLevel().music != null) {
            Level.getCurrentLevel().music.play(true, Level.getCurrentLevel().musicVolume);
        }
    }

    protected void init() {
        player = (Player) new Player.Builder().position(400, 300)
                .sprite(Sprite.playerIdle[0]).animation(Animation.playerIdle)
                .build();
        camera = new Vec2d();
    }

    public void update() {
        if (Game.game.input.resetDown && !Game.game.input.resetWasDown) {
            Level.getCurrentLevel().restart();
        }

        Level.getCurrentLevel().update();
        player.update();

        camera.x = Math.floor(player.position.x / (double) Game.WIDTH) * Game.WIDTH;
        camera.y = Math.floor(player.position.y / (double) Game.HEIGHT) * Game.HEIGHT;
    }

    public void render(GL2 gl) {
        Level.getCurrentLevel().render(gl);
//      RenderUtils.drawText("asdfasdfasdfasdasdfasdf", new Vec2d(200, 1), new Color4f(1, 1, 1, 1), RenderUtils
//              .trajan36);
    }

    public Player getPlayer() {
        return player;
    }

}
