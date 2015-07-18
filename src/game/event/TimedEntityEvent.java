package game.event;

import game.entity.Entity;

public class TimedEntityEvent implements EntityEvent {

    private int delay;
    private long ticks;
    private EntityEvent event;

    public TimedEntityEvent(int delay, EntityEvent event) {
        this.delay = delay;
        this.event = event;
        ticks = 0;
    }

    public void activate(Entity entity) {
        if(ticks % delay == 0)
            event.activate(entity);
        ticks++;
    }

    public void restart() {
        ticks = 0;
    }
}
