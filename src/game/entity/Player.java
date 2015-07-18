package game.entity;

import com.jogamp.opengl.GL2;
import game.Game;
import game.collision.PolygonHitbox;
import game.collision.RectangularHitbox;
import game.graphics.Animation;
import game.graphics.Shader;
import game.level.Level;
import game.level.block.Block;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player extends Entity {

    public static enum State {
        GROUNDED, AIRBORNE, DYING
    }

    public static enum SpriteDirection {
        LEFT, RIGHT
    }

    public State state = State.AIRBORNE;

    private double walkSpeed = 3;
    private double gravity = 0.4d;
    private double jumpVelocity = -8.5;
    private double doubleJumpVelocity = -7;
    public double jumpsUsed = 0;
    public double maxJumps = 1;
    private double maxFallSpeed = 9;

    private SpriteDirection spriteDirection = SpriteDirection.RIGHT;
    private boolean flipHorizontal = false;

    private long deathTime; // Time of death, used in death shader

    protected Player(Builder builder) {
        //size = 12,19
        super(builder);
        setAnimation(Animation.playerIdle);
    }

    public void die() {
        if(state == State.DYING || !active) return;
        deathTime = System.currentTimeMillis();
        active = false;
        state = State.DYING;
    }

    public void update() {
        if(!active) return;

        acceleration.y = gravity;

        velocity.x += acceleration.x;
        position.x += velocity.x;

        if (Game.game.input.rightDown) {
            position.x += walkSpeed;
            spriteDirection = SpriteDirection.RIGHT;
        } else if (Game.game.input.leftDown) {
            position.x -= walkSpeed;
            spriteDirection = SpriteDirection.LEFT;
        }

        handleJumping();

        updateBounds();
        handleBlockCollisions('x');
        updateBounds();
        if(!active) return;

        if (state == State.AIRBORNE) {
            if(velocity.y > maxFallSpeed) velocity.y = maxFallSpeed;
            velocity.y += acceleration.y;
            position.y += velocity.y;
        }

        updateBounds();
        handleBlockCollisions('y');
        updateBounds();
        if(!active) return;

        handleEntityCollisions();
        if(!active) return;

        sprite = animation.nextSprite();
        updateSprite();
    }

    private void handleJumping() {
        if (Game.game.input.jumpDown && !Game.game.input.jumpWasDown && jumpsUsed < maxJumps) {
            velocity.y = state == State.GROUNDED ? jumpVelocity : doubleJumpVelocity;
            jumpsUsed++;
            state = State.AIRBORNE;
        }

        if (state == State.AIRBORNE) {
            if (!Game.game.input.jumpDown) {
                if(Game.game.input.jumpWasDown && velocity.y < 0) {
                    velocity.y *= 0.45;
                }
            }
        } else {
            jumpsUsed = 0;
        }
    }

    private void updateBounds() {
        hitbox.position.setEqual(position);
        hitbox.updateBounds();
    }

    //TODO: Rewrite & abstract this collision handling, make Player just another entity in level.entities, do the same with blocks
    private void handleBlockCollisions(char axis) {
        if (axis != 'x' && axis != 'y') {
            System.err.println("Invalid axis!");
            return;
        }


        boolean onGround = false;
        ArrayList<Block> specialBlocks = new ArrayList<>();

        for (Block block : Level.getCurrentLevel().blocks) {
            if(!block.active || !block.isCollidable()) continue;


            RectangularHitbox blockHitbox = block.getHitbox();
            PolygonHitbox blockHitbox2 = block.getPolygonHitbox();

            if (!hitbox.collides(block.getHitbox())) {
                if(block.isCollidable() && !block.hasSpecialHitbox() && block.solidTop && axis == 'y') {
                    if(block.getHitbox().bounds.contains(position.x, position.y + height + 1)
                            || block.getHitbox().bounds.contains(position.x + width, position.y + height + 1)) {
                        onGround = true;
                        position.plusEquals(block.velocity);
                    }
                    if(block.getHitbox().bounds.contains(position.x, position.y + height + 1)
                            && block.getHitbox().bounds.contains(position.x + width, position.y + height + 1)) {
                        block.collide(this);
                    }
                }
                continue;
            }

            if (!block.solid) {
                block.collide(this);
            }

            Rectangle2D intersection = hitbox.bounds.createIntersection(blockHitbox.bounds);
            double w = intersection.getWidth();
            double h = intersection.getHeight();
            if(block.solid) {
                switch (axis) {
                    case 'x':
                        if(block.hasSpecialHitbox()) {
                            specialBlocks.add(block);
                            break;
                        }
                        if (w >= h || block.position.y > position.y - velocity.y + height)
                            break; //No horizontal collision
                        if (position.x < blockHitbox.position.x && block.solidLeft) {
                            //Collision to right of player
                            position.x = blockHitbox.position.x - width;
                        } else if (position.x > blockHitbox.position.x && block.solidRight) {
                            //Collision to left of player
                            position.x = blockHitbox.position.x + blockHitbox.width;
                        }
                        block.collide(this);
                        break;
                    case 'y':
                        if (position.y > blockHitbox.position.y && block.solidBottom) {
                            //Collision above player
                            if(block.hasSpecialHitbox()) {
                                specialBlocks.add(block);
                                break;
                            }
                            position.y = blockHitbox.position.y + blockHitbox.height;
                            velocity.y = 0;
                            block.collide(this);
                        } else if (position.y <= blockHitbox.position.y && block.solidTop) {
                            //Collision below player
                            if(!block.solidBottom
                                    && position.y + height - velocity.y - acceleration.y >
                                    blockHitbox.position.y - block.velocity.y)
                                break;
                            if (block.hasSpecialHitbox()) {
                                specialBlocks.add(block);
                                break;
                            }
                            position.y = blockHitbox.position.y - height;
                            velocity.y = 0;
                            state = State.GROUNDED;
                            onGround = true;
                            block.collide(this);
                        }
                        break;
                }
            }
        }
        updateBounds();
        if(axis == 'y') {
            if (!onGround) {
                if (state == State.GROUNDED)
                    jumpsUsed++;
                state = State.AIRBORNE;
            }
            for(Block block : specialBlocks) {
                if (hitbox.collides(block.getPolygonHitbox())) {
                    block.collide(this);
                }
            }
            specialBlocks.clear();
        }
    }
    private void handleEntityCollisions() {
        Level.getCurrentLevel().entities.stream().filter((e) -> e.isCollidable() && e.active).forEach((entity) -> {
            if (collides(entity)) {
                entity.collide(this);
            }
        });
    }

    public void render(GL2 gl) {
        if(state == State.DYING) {
            Shader.deathShader.enable(gl);
            gl.glUniform1i(Shader.deathShader.getUniform("texture1", gl), 0);
            gl.glUniform1f(Shader.deathShader.getUniform("time", gl),
                    (float) ((deathTime - System.currentTimeMillis()) % 100000));
        }
        super.render(flipHorizontal, false, gl);
        if(state == State.DYING) Shader.deathShader.disable(gl);
    }

    public void updateSprite() {
        flipHorizontal = spriteDirection == SpriteDirection.LEFT;
        if(state == State.GROUNDED) {
            if (Game.game.input.leftDown || Game.game.input.rightDown) setAnimation(Animation.playerRun);
            else setAnimation(Animation.playerIdle);
        } else if(state == State.AIRBORNE) {
            if(velocity.y < 0) setAnimation(Animation.playerJump);
            else setAnimation(Animation.playerFall);
        }
    }

    public static class Builder extends Entity.Builder {
        {
            size(12, 19);
        }
        public Entity build() {
            return new Player(this);
        }
    }
}
