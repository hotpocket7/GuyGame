package game.level.block;

import game.Game;
import game.graphics.Color4f;
import game.entity.Entity;
import game.entity.Player;
import game.graphics.light.RadialLight;
import game.graphics.Sprite;
import game.level.Level;
import game.math.Vec2d;

public class BlockSave extends Block {

    private Sprite normalSprite, savedSprite;
    private int frameCount = 0;

    private RadialLight light;
    private float lightIntensity;
    private Color4f lightColor;

    private boolean lightActive = false;
    private long saveTime;

    private BlockSave(Builder builder) {
        super(builder);
        normalSprite = builder.normalSprite;
        savedSprite = builder.savedSprite;
        lightIntensity = builder.lightIntensity;
        lightColor = builder.lightColor;
    }

    protected void onCollide(Entity entity) {
        if(entity instanceof Player && Game.game.input.saveDown && !Game.game.input.saveWasDown) {
            save();
        }
    }

    public void save() {
        Level.getCurrentLevel().spawn.setEqual(Game.scene.getPlayer().position);
        sprite = savedSprite;
        if(!lightActive) {
            frameCount = 0;
            light = new RadialLight(new Vec2d(position.x + width / 2, position.y + height / 2 + 4),
                    50, lightColor, Level.getCurrentLevel().ambientColor);
            Level.getCurrentLevel().lights.add(light);
            lightActive = true;
            saveTime = System.currentTimeMillis();
        }
    }

    public void update() {
        if(sprite == savedSprite) {
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
                Level.getCurrentLevel().lights.remove(light);
                lightActive = false;
            }
        }
    }

    public static class Builder extends Block.Builder {
        protected Sprite normalSprite, savedSprite;
        protected float lightIntensity;
        protected Color4f lightColor;

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

        public Block build() {
            return new BlockSave(this);
        }
    }
}
