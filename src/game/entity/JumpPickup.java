package game.entity;

import com.jogamp.opengl.GL2;
import game.graphics.Shader;
import game.sound.Sounds;

public class JumpPickup extends Pickup {

    public static final int DEFAULT_WIDTH = 18, DEFAULT_HEIGHT = 18;
    private int timer = 0;

    private JumpPickup(Builder builder) {
        super(builder);
    }

    public void update() {
        if(!dying)
            super.update();
        else {
            if(timer < 15) {
                if(velocity.y > 0)
                    velocity.y = 0;
                velocity.y -= 0.75;
                velocity.x = 0;
                addToPos(velocity);
                timer += 2;
                alpha -= 0.15;
            } else {
                active = false;
                dying = false;
                alpha = 1;
            }
        }
    }

    protected void onCollide(Entity entity) {
        boolean d = dying;
        super.onCollide(entity);
        if(entity instanceof Player && !d) {
            Sounds.pickupJumpRefresher.play(0.5);
            Player player = (Player) entity;
            player.jumpsUsed = player.maxJumps - 1;
        }
    }

    public void render(GL2 gl) {
        Shader.floatShader.enable(gl);
        gl.glUniform1f(Shader.deathShader.getUniform("time", gl),
                (float) (System.currentTimeMillis() % 2000));
        gl.glUniform1f(Shader.floatShader.getUniform("wtff", gl), (float) alpha);
        gl.glUniform1i(Shader.floatShader.getUniform("texture1", gl), 0);
        super.render(gl);
        Shader.floatShader.disable(gl);
    }

    public void respawn() {
        super.respawn();
        timer = 0;
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
