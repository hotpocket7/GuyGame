package game.entity;

import com.jogamp.opengl.GL2;
import game.graphics.Shader;
import game.sound.Sounds;

public class JumpPickup extends Pickup {

    public static final int DEFAULT_WIDTH = 18, DEFAULT_HEIGHT = 18;

    private JumpPickup(Builder builder) {
        super(builder);
    }

    protected void onCollide(Entity entity) {
        super.onCollide(entity);
        if(entity instanceof Player) {
            Sounds.pickupJumpRefresher.play(0.5);
            Player player = (Player) entity;
            player.jumpsUsed = player.maxJumps - 1;
        }
    }

    public void render(boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        Shader.floatShader.enable(gl);
        gl.glUniform1i(Shader.floatShader.getUniform("texture1", gl), 0);
        gl.glUniform1f(Shader.deathShader.getUniform("time", gl),
                (float) (System.currentTimeMillis() % 2000));
        super.render(flipHorizontal, flipVertical, gl);
        Shader.floatShader.disable(gl);
    }

    public static class Builder extends Entity.Builder {
        {
            width = DEFAULT_WIDTH;
            height = DEFAULT_HEIGHT;
            collidable = true;
        }

        public Entity build() {
            return new JumpPickup(this);
        }
    }
}
