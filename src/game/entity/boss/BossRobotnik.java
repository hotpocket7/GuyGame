package game.entity.boss;

import game.entity.Entity;
import game.event.Events;
import game.graphics.Animation;
import game.graphics.Sprite;
import game.level.Level;

public class BossRobotnik extends Boss {

    public static int WIDTH = 154;
    public static int HEIGHT = 116;

    private static Attack enter = new Attack() {
        @Override
        protected void step(Boss b, long t) {
            if(t == 0) {
                b.velocity.y = 10;
                b.flippedHorizontal = true;
                b.setAnimation(Animation.boss0Idle);
            }
            b.velocity.y -= 0.075;
            if(b.getYPos() > 1332-64) {
                done = true;
            }
        }
    };

    private static Attack move = new Attack() {
        double initialYVel;
        @Override
        protected void step(Boss b, long t) {
            Level level = Level.getCurrentLevel();
            if(t == 0) {
                b.setAnimation(Animation.boss0Moving);
                initialYVel = b.velocity.y;
            }

            if(t <= 20) {
                b.velocity.y -= initialYVel / 20;
            } else {
                b.velocity.y = 0;
            }

            if(t >= 0 && t <= 10)
                b.velocity.x += 1.5;

            if(b.getXPos() < 5824)
                b.velocity.x += 1.5;
            if(b.getXPos() > 6048)
                b.velocity.x -= 1.5;

            if(t > 20 && t % 7 == 0) {
                level.addEntity(
                        new Entity.Builder().sprite(Sprite.fireProjectile[0]).animation(Animation.fireProjectile)
                                .position(b.getPos().add(48, 96)).velocity(random(-20, 20)/10, 3).acceleration(0, 0.5)
                                .size(17, 17).temporary(true)
                                .addUpdateEvents(Events.destroyOnLeaveScreen)
                                .addCollisionEvent(Events.killPlayerOnTouch).collidable(true).build()
                );
            }

            if(b.velocity.x >= 0)
                b.flippedHorizontal = true;
            else
                b.flippedHorizontal = false;
        }
    };

    private BossRobotnik(Builder builder) {
        super(builder);
    }

    public static class Builder extends Boss.Builder {

        {
            size(WIDTH, HEIGHT);
            sprite(Sprite.boss0Idle[0]);
            animation(Animation.boss0Idle);
            temporary(true);
            updateOffScreen(true);
            attacks(enter, move);
        }

        public Entity build() {
            return new BossRobotnik(this);
        }
    }
}
