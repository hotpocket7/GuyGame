package game.level.block;

import game.Game;
import game.graphics.Color4f;
import game.entity.Entity;
import game.entity.Player;
import game.graphics.light.RadialLight;
import game.graphics.Sprite;
import game.level.Level;
import game.math.Vec2d;
import game.sound.Sounds;
import kuusisto.tinysound.Sound;

public class BlockSave extends Block {

    private Sprite normalSprite, savedSprite;
    private int frameCount = 0;

    private RadialLight light;
    private float lightIntensity;
    private Color4f lightColor;

    private long saveTime;
    private boolean saving = false;

    private Sound sound;

    private BlockSave(Builder builder) {
        super(builder);
        normalSprite = builder.normalSprite;
        savedSprite = builder.savedSprite;
        lightIntensity = builder.lightIntensity;
        lightColor = builder.lightColor;
        sound = builder.sound;
        if(sound == null)
            sound = Sounds.save;
    }

    protected void onCollide(Entity entity) {
        if(entity instanceof Player && Game.game.input.keyHit("save")) {
            save();
            callEvents("onSave");
            saving = true;
            sound.play(0.2);
        }
    }

    public void save() {
        Level.getCurrentLevel().spawn.setEqual(Game.screen.getPlayer().getPos().subtract(Game.screen.getPlayer()
                .velocity)); // Subtract velocity to keep player from saving inside blocks
        sprite = savedSprite;
        if(!saving) {
            frameCount = 0;
            light = new RadialLight(new Vec2d(getXPos() + width / 2, getYPos() + height / 2 + 4),
                    50, lightColor, Level.getCurrentLevel().ambientColor);
            Level.getCurrentLevel().lights.add(light);
            saving = true;
            saveTime = System.currentTimeMillis();
        }
    }

    public void update() {
        super.update();
        if(saving) {
            light.position.setEqual(getXPos() + width / 2, getYPos() + height / 2 + 4);
            frameCount++;
            if(frameCount < 10) {
                light.color.a += 0.03 * lightIntensity;
            }
            if(frameCount > 40) {
                light.color.a -= 0.03 * lightIntensity;
            }
            light.xRadius += 2*Math.sin((System.currentTimeMillis() - saveTime) * 2 * Math.PI / 500);
            light.yRadius += 2*Math.cos((System.currentTimeMillis() - saveTime) * 2 * Math.PI / 500);
            if(frameCount > 50) {
                sprite = normalSprite;
                frameCount = 0;
                saving = false;
                Level.getCurrentLevel().lights.remove(light);
            }
        }
    }

    public void respawn() {
        super.respawn();
        if(saving) {
            Level.getCurrentLevel().lights.remove(light);
            saving = false;
            sprite = normalSprite;
            frameCount = 0;
        }
    }

    public boolean isSaving() {
        return saving;
    }

    public static class Builder extends Block.Builder {

        protected Sprite normalSprite, savedSprite;
        protected float lightIntensity;
        protected Color4f lightColor;
        protected Sound sound;

        public Builder() {
            solid = false;
            collidable = true;
            lightColor = new Color4f(0, 1, 0, 0);
            lightIntensity = 0.6f;
        }

        public Builder normalSprite(Sprite normalSprite) {
            this.normalSprite = normalSprite;
            this.sprite = normalSprite;
            return this;
        }

        public Builder savedSprite(Sprite savedSprite) {
            this.savedSprite = savedSprite;
            return this;
        }

        public Builder lightIntensity(float lightIntensity) {
            this.lightIntensity = lightIntensity;
            return this;
        }

        public Builder sound(Sound sound) {
            this.sound = sound;
            return this;
        }

        public Block build() {
            return new BlockSave(this);
        }
    }
}
