package game.graphics;

import com.jogamp.opengl.GL2;
import game.math.Vec2d;

import java.awt.*;

public class Sprite {

    public static Sprite[] playerIdle, playerRun, playerJump, playerFall;
    public static Sprite[] boss0Moving, boss0Idle;
    public static Sprite[] torch;

    public static Sprite menuBG, menuTitle;

    public static void loadSprites() {
        playerIdle = SpriteSheet.playerIdle.split();
        for(int i = 0; i <= 3; i++) {
            playerIdle[i].setOffset(-11, 13);
            playerIdle[i].setOffsetSize(22, 26);
        }

        playerRun = SpriteSheet.playerRun.split();
        for(int i = 0; i <= 3; i++) {
            playerRun[i].setOffset(-13, 12);
            playerRun[i].setOffsetSize(22, 24);
        }

        playerJump = SpriteSheet.playerJump.split();
        for(int i = 0; i <= 1; i++) {
            playerJump[i].setOffset(-11, 10);
            playerJump[i].setOffsetSize(22, 20);
        }

        playerFall = SpriteSheet.playerFall.split();
        for(int i =  0; i <= 1; i++) {
            playerFall[i].setOffset(-12, 13);
            playerFall[i].setOffsetSize(22, 26);
        }

        torch = SpriteSheet.tileSet1.split(40, 47);

        boss0Moving = SpriteSheet.boss0.split(0, 3);
        boss0Idle = SpriteSheet.boss0.split(4, 7);
        for(int i = 0; i <= 3; i++) {
            boss0Moving[i].setOffsetSize(124, 100);
            boss0Moving[i].setOffsetSize(124, 100);
        }

        menuBG = new Sprite(SpriteSheet.menuBG);
        menuTitle = new Sprite(SpriteSheet.menuTitle);
    }

    private SpriteSheet spriteSheet;
    public int index;

    private Vec2d offset = new Vec2d();
    private Vec2d origin = new Vec2d();
    private Vec2d center = new Vec2d();
    private Dimension size;
    private int offsetWidth = 0, offsetHeight = 0;

    public Sprite(SpriteSheet sheet) {
        this.spriteSheet = sheet;
        this.index = 0;
    }

    public Sprite(SpriteSheet spriteSheet, int index) {
        this.spriteSheet = spriteSheet;
        this.index = index;
    }

    public Sprite(SpriteSheet spriteSheet, int index, int xOffset, int yOffset, int width, int height) {
        this.spriteSheet = spriteSheet;
        this.index = index;
        setOffset(xOffset, yOffset);
        setOffsetSize(width, height);
    }

    private Sprite(Builder builder) {
        spriteSheet = builder.spriteSheet;
        index = builder.index;

        origin = builder.origin;
        center = builder.center;
        size = builder.size;
    }

    public void render(Vec2d position, boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        spriteSheet.renderSprite(index, position, offset, offsetWidth, offsetHeight, flipHorizontal, flipVertical, gl);
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    //Setters
    private void setOffset(double x, double y) {
        this.offset.setEqual(x, y);
    }
    private void setOffsetSize(int width, int height) {
        offsetWidth = width;
        offsetHeight = height;
    }

    public static class Builder {
        private SpriteSheet spriteSheet;
        public int index;

        private Vec2d origin = new Vec2d();
        private Vec2d center = new Vec2d();
        private Dimension size;

        public Sprite build() {
            return new Sprite(this);
        }

        public Builder spriteSheet(SpriteSheet spriteSheet) {
            this.spriteSheet = spriteSheet;
            return this;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder origin(double x, double y) {
            this.origin = new Vec2d(x, y);
            return this;
        }

        public Builder origin(Vec2d origin) {
            this.origin = new Vec2d(origin);
            return this;
        }

        public Builder center(double x, double y) {
            this.center = new Vec2d(x, y);
            return this;
        }

        public Builder center(Vec2d center) {
            this.center = new Vec2d(center);
            return this;
        }

        public Builder size(Dimension size) {
            this.size = size;
            return this;
        }

        public Builder size(int width, int height) {
            this.size = new Dimension(width, height);
            return this;
        }
    }
}
