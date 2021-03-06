package game.entity;

import com.jogamp.opengl.GL2;
import game.Game;
import game.collision.CollisionHandler;
import game.graphics.Animation;
import game.graphics.Shader;
import game.level.Level;
import game.level.block.Block;
import game.sound.Sounds;

import java.util.ArrayList;

public class Player extends Entity {

    public static enum State {
        GROUNDED, AIRBORNE, DYING
    }

    public static enum SpriteDirection {
        LEFT, RIGHT
    }

    public State state = State.AIRBORNE;
    public State previousState = State.AIRBORNE;

    private double walkSpeed = 3;
    private double gravity = 0.4d;
    private double jumpVelocity = -8.5;
    private double doubleJumpVelocity = -7;
    public double jumpsUsed = 0;
    public double maxJumps = 1;
    private double maxFallSpeed = 9;

    private SpriteDirection spriteDirection = SpriteDirection.RIGHT;

    private long deathTime; // Time of death, used in death shader

    protected Player(Builder builder) {
        super(builder);
        setAnimation(Animation.playerIdle);
    }

    public void die() {
        if(state == State.DYING || !active) return;
        Sounds.playerDeath.play(0.4);
        deathTime = System.currentTimeMillis();
        active = false;
        state = State.DYING;
    }

    public void update() {
        if(!active) return;

        setYAccel(gravity);

        velocity.x += acceleration.x;
        addToPos(getXVel(), 0);

        if (Game.game.input.keyDown("right")) {
            addToPos(walkSpeed, 0);
            spriteDirection = SpriteDirection.RIGHT;
            velocity.x = 0;
        } else if (Game.game.input.keyDown("left")) {
            addToPos(-walkSpeed, 0);
            spriteDirection = SpriteDirection.LEFT;
            velocity.x = 0;
        }

        handleJumping();

        handleBlockCollisions('x');
        if(!active) return;

        if (state == State.AIRBORNE) {
            if(getYVel() > maxFallSpeed) velocity.y = maxFallSpeed;
            velocity.y += acceleration.y;
            addToPos(0, getYVel());
        }

        handleBlockCollisions('y');
        if(!active) return;

        handleEntityCollisions();
        if(!active) return;

        sprite = animation.nextSprite();
        updateSprite();
        previousState = state;
    }

    private void handleJumping() {
        if (Game.game.input.keyHit("jump") && jumpsUsed < maxJumps) {
            if(state == State.GROUNDED) {
                Sounds.playerJump.play();
                velocity.y = jumpVelocity;
            } else if(state == State.AIRBORNE) {
                Sounds.playerDoubleJump.play();
                velocity.y = doubleJumpVelocity;
            }
            jumpsUsed++;
            state = State.AIRBORNE;
        }

        if (state == State.AIRBORNE) {
            if (Game.game.input.keyReleased("jump") && velocity.y < 0) {
                velocity.y *= 0.45;
            }
        } else if(state == State.GROUNDED) {
            jumpsUsed = 0;
        }
    }

    private void handleBlockCollisions(char axis) {
        updateBounds();
        state = State.AIRBORNE;

        ArrayList<Block> specialBlocks = new ArrayList<>();

        for(Block block : Level.getCurrentLevel().blocks) {
            if(!block.isOnScreen())
                continue;
            if(collides(block)) {
                if(block.isSpecial()) {
                    specialBlocks.add(block);
                    continue;
                }

                if(block.isCollidable() && axis == 'y')
                    block.collide(this);
                if(!active) return;
                if(axis == 'x')
                    CollisionHandler.resolveCollisionX(this, block);
                if(axis == 'y')
                    CollisionHandler.resolveCollisionY(this, block);
            }
        }

        for(Block block : specialBlocks) {
            if(!collides(block))
                continue;

            COLLIDE:
            {
                if (block.isCollidable()) {
                    if (block.hasSpecialHitbox() && axis != 'y')
                        break COLLIDE;
                    block.collide(this);
                }
            }
            if(!active) return;
            if(axis == 'x')
                CollisionHandler.resolveCollisionX(this, block);
            if(axis == 'y')
                CollisionHandler.resolveCollisionY(this, block);
        }

        if(axis == 'y')
            if(state == State.AIRBORNE && previousState == State.GROUNDED && !Game.game.input.keyDown("jump")) {
                jumpsUsed++;
            } else if(state == State.GROUNDED) {
                jumpsUsed = 0;
            }
    }

    private void handleEntityCollisions() {
        pollCollisions(Level.getCurrentLevel().entities);
    }

    public void render(GL2 gl) {
        if(state == State.DYING) {
            Shader.deathShader.enable(gl);
            gl.glUniform1i(Shader.deathShader.getUniform("texture1", gl), 0);
            gl.glUniform1f(Shader.deathShader.getUniform("time", gl),
                    (float) ((deathTime - System.currentTimeMillis()) % 100000));
        }
        super.render(gl);
        if(state == State.DYING) {
            Shader.deathShader.disable(gl);
        }
    }

    public void updateSprite() {
        flippedHorizontal = spriteDirection == SpriteDirection.LEFT;
        if(state == State.GROUNDED) {
            if (Game.game.input.keyDown("left") || Game.game.input.keyDown("right")) setAnimation(Animation.playerRun);
            else setAnimation(Animation.playerIdle);
        } else if(state == State.AIRBORNE) {
            if(velocity.y < 0) setAnimation(Animation.playerJump);
            else setAnimation(Animation.playerFall);
        }
    }

    public static class Builder extends Entity.Builder {
        {
            size(11, 21);
        }
        public Entity build() {
            return new Player(this);
        }
    }
}
