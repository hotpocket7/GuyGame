package game.entity.boss;

import game.entity.Entity;

public abstract class Attack {

    private long timer = 0;
    protected boolean done;

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
}
