package game.entity.boss;

import game.entity.Entity;
import game.graphics.Animation;
import game.graphics.Sprite;

public class BossRobotnik extends Boss {

    private static Attack move = new Attack() {
        @Override
        protected void step(Boss b, long t) {
            if(t == 0) {
                b.velocity.x = 5;
                b.flippedHorizontal = true;
                b.setAnimation(Animation.boss0Moving);
            }
            else if(t % 60 == 0) {
                b.velocity.x *= -1;
                b.flippedHorizontal = !b.flippedHorizontal;
            }
        }
    };

    private BossRobotnik(Builder builder) {
        super(builder);
    }

    public static class Builder extends Boss.Builder {

        {
            size(154, 116);
            sprite(Sprite.boss0Idle[0]);
            animation(Animation.boss0Idle);
            temporary(true);
            updateOffScreen(true);
            attacks(move);
        }

        public Entity build() {
            return new BossRobotnik(this);
        }
    }
}
