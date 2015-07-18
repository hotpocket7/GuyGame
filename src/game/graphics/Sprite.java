package game.graphics;

import com.jogamp.opengl.GL2;
import game.math.Vec2d;

public class Sprite {

    public static Sprite[] playerIdle, playerRun, playerJump, playerFall;
    public static Sprite[] torch;

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
    }

    private SpriteSheet spriteSheet;
    public int index;

    private Vec2d offset = new Vec2d();
    private int offsetWidth = 0, offsetHeight = 0;

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

    public void render(Vec2d position, boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        spriteSheet.renderSprite(index, position, offset, offsetWidth, offsetHeight, flipHorizontal, flipVertical, gl);
    }

    //Setters
    private void setOffset(double x, double y) {
        this.offset.setEqual(x, y);
    }
    private void setOffsetSize(int width, int height) {
        offsetWidth = width;
        offsetHeight = height;
    }
}
