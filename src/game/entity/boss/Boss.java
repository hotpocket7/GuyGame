package game.entity.boss;

import game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class Boss extends Entity {

    private List<Attack> attacks;
    private Attack currentAttack;

    protected Boss(Builder builder) {
        super(builder);
        this.attacks = builder.attacks;
        currentAttack = attacks.get(0);
    }

    public void update() {
        super.update();
        currentAttack.update(this);
        if(currentAttack.done()) {
            int nextIndex = attacks.indexOf(currentAttack) + 1;
            if(nextIndex >= attacks.size())
                nextIndex = 0;
            currentAttack = attacks.get(nextIndex);
        }
    }

    public void respawn() {
        super.respawn();
        attacks.forEach(Attack::restart);
    }

    public static abstract class Builder extends Entity.Builder {

        private List<Attack> attacks = new ArrayList<>();

        public Builder attacks(Attack... attacks) {
            for (Attack attack : attacks) {
                this.attacks.add(attack);
            }
            return this;
        }
    }
}
