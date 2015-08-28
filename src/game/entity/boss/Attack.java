package game.entity.boss;

import game.entity.Entity;

import java.util.Random;

public abstract class Attack {

    private long timer = 0;
    protected boolean done;
    private Random random = new Random();

    public void update(Boss boss) {
        step(boss, timer);
        timer++;
    }

    protected abstract void step(Boss b, long t);

    public boolean done() {
        return done;
    }

    public void restart() {
        timer = 0;
        done = false;
    }

    public double random(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
