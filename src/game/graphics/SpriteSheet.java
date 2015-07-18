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

import static java.lang.Math.*;

public class SpriteSheet {

    public static SpriteSheet playerIdle, playerRun, playerJump, playerFall;
    public static SpriteSheet tileSet1, tileSet2;
    public static SpriteSheet pickupSheet;
    public static SpriteSheet levelBG1, levelBG2;

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

        levelBG1 = new SpriteSheet("/backgrounds/bg1.png", 800, 608);
        levelBG2 = new SpriteSheet("/backgrounds/bg2.png", 800, 608);
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

    public void renderSprite(int index, Vec2d position, Vec2d offset,
                             int offsetWidth, int offsetHeight,
                             boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        Vec2d camera = Game.scene.getCamera();
        if(position.x + spriteWidth < camera.x
                || position.y + spriteHeight  < camera.y
                || position.x > camera.x + Game.WIDTH
                || position.y > camera.y + Game.HEIGHT)
            return;

        float x = (float) position.x;
        float y = (float) position.y;

        y = Game.scene.getHeight() - y - spriteHeight; //Origin in-game is top-left rather than bottom-left
        float xx = (index % numSpritesX) * (float) spriteWidth / width;
        float yy = (float) Math.floor(index / numSpritesX) * (float) spriteHeight / height;

        Vec2d finalOffset = new Vec2d(offset.x, offset.y);

        if(flipHorizontal)
            finalOffset.x = signum(offset.x) * (abs(offsetWidth) - abs(offset.x));
        if(flipVertical)
            finalOffset.y = signum(offset.y) * (abs(offsetHeight) - abs(offset.y));

        x += finalOffset.x;
        y += finalOffset.y;

//      if(flipHorizontal) System.out.println("New offset: " + offset.toString());

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL2.GL_TEXTURE_2D);

        texture.enable(gl);
        texture.bind(gl);

        float x1 = flipHorizontal ? x + spriteWidth : x;
        float y1 = flipVertical ? y + spriteHeight : y;
        float x2 = flipHorizontal ? x : x + spriteWidth;
        float y2 = flipVertical ? y : y + spriteHeight;

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor4f(1, 1, 1, 1);

        gl.glTexCoord2f(xx, yy+(float)spriteHeight/height);
        gl.glVertex2f(x1, y1);

        gl.glTexCoord2f(xx, yy);
        gl.glVertex2f(x1, y2);

        gl.glTexCoord2f(xx + (float) spriteWidth/width, yy);
        gl.glVertex2f(x2, y2);

        gl.glTexCoord2f(xx + (float) spriteWidth/width, yy + (float) spriteHeight/height);
        gl.glVertex2f(x2, y1);

        gl.glEnd();
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_BLEND);

    }

    private void load() {
        try {
            image = new File("asdf"); //Need something to copy the image to
            InputStream is = SpriteSheet.class.getResourceAsStream(path);
            FileUtils.copyInputStreamToFile(is, image);
            texture = TextureIO.newTexture(image, true);
            BufferedImage bi = ImageIO.read(image);
            width = bi.getWidth();
            height = bi.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sprite[] split(int startIndex, int endIndex) {
        Sprite[] sprites = new Sprite[endIndex-startIndex + 1];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(this, i + startIndex);
        }
        return sprites;
    }

    public Sprite[] split() {
        return split(0, numSprites-1);
    }

    public int getNumSprites() {
        return numSprites;
    }

}
