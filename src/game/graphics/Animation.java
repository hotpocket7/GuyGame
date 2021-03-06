package game.graphics;

import game.Game;
import game.Screen;

import java.util.Arrays;

public class Animation {

    public static Animation playerIdle, playerRun, playerJump, playerFall;
    public static Animation torch;

    public static Animation boss0Idle, boss0Moving;
    public static Animation fireProjectile;

    static {
        playerIdle = new Animation(Sprite.playerIdle, 10);
        playerRun = new Animation(Sprite.playerRun, 10);
        playerJump = new Animation(Sprite.playerJump, 10);
        playerFall = new Animation(Sprite.playerFall, 10);

        boss0Idle = new Animation(Sprite.boss0Idle, 5);
        boss0Moving = new Animation(Sprite.boss0Moving, 5);

        fireProjectile = new Animation(Sprite.fireProjectile, 10);

        torch = new Animation(Sprite.torch, 10);
    }

    private Sprite[] sprites;
    private int fps;

    private int currentIndex = 0;

    public Animation(Sprite[] sprites, int fps) {
        this.sprites = new Sprite[sprites.length];
        System.arraycopy(sprites, 0, this.sprites, 0, sprites.length);
        this.fps = fps;
    }

    public Sprite nextSprite() {
        if(Screen.getTotalUpdates() % Math.ceil((float) Game.fps / fps) != 0) return sprites[currentIndex];

        currentIndex++;
        currentIndex %= sprites.length;
        return sprites[currentIndex];
    }

    public void restart() {
        currentIndex = 0;
    }

    public Animation clone() {
        return new Animation(sprites, fps);
    }

    public void setEqual(Animation animation) {
        sprites = new Sprite[animation.sprites.length];
        System.arraycopy(animation.sprites, 0, sprites, 0, sprites.length);
    }

    public boolean equals(Animation animation) {
        if(animation == null)
            return false;
        return Arrays.equals(sprites, animation.sprites) && fps == animation.fps;
    }

}
