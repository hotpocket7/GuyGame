package game.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import game.Game;
import game.math.Vec2d;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class SpriteSheet {

    public static SpriteSheet playerIdle, playerRun, playerJump, playerFall;

    public static SpriteSheet tileSet1, tileSet2;
    public static SpriteSheet pickupSheet;
    public static SpriteSheet levelBG1, levelBG2;
    public static SpriteSheet menuBG, menuTitle;

    public static SpriteSheet boss0;
    public static SpriteSheet fireProjectile;

    public static SpriteSheet lastSheet;

    public static void loadSpriteSheets() {
        //Blocks
        tileSet1 = new SpriteSheet("/sprites/block1.png", 32, 32);
        tileSet2 = new SpriteSheet("/sprites/block2.png", 32, 32);

        //Items
        pickupSheet = new SpriteSheet("/sprites/pickups.png", 32, 32);

        // Player
        playerIdle = new SpriteSheet("/sprites/kid/idle.png", 32, 32);
        playerRun = new SpriteSheet("/sprites/kid/run.png", 32, 32);
        playerJump = new SpriteSheet("/sprites/kid/jump.png", 32, 32);
        playerFall = new SpriteSheet("/sprites/kid/fall.png", 32, 32);

        // Boss 0
        boss0 = new SpriteSheet("/sprites/boss0.png", 154, 116);

        // Projectiles
        fireProjectile = new SpriteSheet("/sprites/fire.png", 24, 48);

        levelBG1 = new SpriteSheet("/backgrounds/bg1.png", 800, 608);
        levelBG2 = new SpriteSheet("/backgrounds/bg2.png", 800, 608);

        menuBG = new SpriteSheet("/backgrounds/menu.png", 800, 608);
        menuTitle = new SpriteSheet("/menu/title.png", 696, 196);
    }

    private String path;
    private int numSpritesX, numSpritesY, numSprites;
    private int spriteWidth, spriteHeight;
    private int width, height;
    private File image;
    private Texture texture;

    public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.path = path;
        load();
        numSpritesX = width / spriteWidth;
        numSpritesY = height / spriteHeight;
        numSprites = numSpritesX * numSpritesY;
    }

    private void load() {
        try {
            image = new File("aksdfjadlsfjasdhfjklasdhk"); //Need something to copy the image to
            InputStream is = SpriteSheet.class.getResourceAsStream(path);
            FileUtils.copyInputStreamToFile(is, image);
            texture = TextureIO.newTexture(image, true);
            BufferedImage bi = ImageIO.read(image);
            width = bi.getWidth();
            height = bi.getHeight();
            image.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sprite[] split(int startIndex, int endIndex) {
        Sprite[] sprites = new Sprite[endIndex - startIndex + 1];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(this, i + startIndex);
        }
        return sprites;
    }

    public Sprite[] split() {
        return split(0, Math.max(0, numSprites - 1));
    }

    public int getNumSprites() {
        return numSprites;
    }

    public int getNumSpritesX() {
        return numSpritesX;
    }

    public int getNumSpritesY() {
        return numSpritesY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public Texture getTexture() {
        return texture;
    }
}
