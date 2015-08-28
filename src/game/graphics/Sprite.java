package game.graphics;

import com.jogamp.opengl.GL2;
import game.Game;
import game.math.Vec2d;

import java.awt.geom.Rectangle2D;

public class Sprite {

    public static Sprite[] playerIdle, playerRun, playerJump, playerFall;
    public static Sprite levelBG1, levelBG2;
    public static Sprite[] torch;
    public static Sprite jumpPickup;

    public static Sprite[] boss0Moving, boss0Idle;
    public static Sprite[] fireProjectile;

    public static Sprite menuBG, menuTitle;

    public static void loadSprites() {
        playerIdle = SpriteSheet.playerIdle.split();
        playerIdle[0].setBounds(3, 11, 24, 21);
        playerIdle[1].setBounds(4, 12, 23, 20);
        playerIdle[2].setBounds(5, 11, 22, 21);
        playerIdle[3].setBounds(4, 11, 23, 21);

        playerIdle[0].setHitbox(11, 11, 11, 21);
        playerIdle[1].setHitbox(11, 11, 11, 21);
        playerIdle[2].setHitbox(11, 11, 11, 21);
        playerIdle[3].setHitbox(11, 11, 11, 21);

        playerRun = SpriteSheet.playerRun.split();
        playerRun[0].setBounds(3, 11, 25, 21);
        playerRun[1].setBounds(3, 11, 25, 21);
        playerRun[2].setBounds(3, 11, 25, 21);
        playerRun[3].setBounds(3, 11, 25, 21);

        playerRun[0].setHitbox(12, 11, 11, 21);
        playerRun[1].setHitbox(12, 11, 11, 21);
        playerRun[2].setHitbox(12, 11, 11, 21);
        playerRun[3].setHitbox(12, 11, 11, 21);

        playerJump = SpriteSheet.playerJump.split();
        playerJump[0].setBounds(7, 9, 18, 23);
        playerJump[1].setBounds(8, 9, 17, 23);

        playerJump[0].setHitbox(10, 9, 11, 21);
        playerJump[1].setHitbox(10, 9, 11, 21);

        playerFall = SpriteSheet.playerFall.split();
        playerFall[0].setBounds(2, 12, 25, 20);
        playerFall[1].setBounds(3, 12, 24, 20);

        playerFall[0].setHitbox(11, 11, 11, 21);
        playerFall[1].setHitbox(11, 11, 11, 21);

        levelBG1 = SpriteSheet.levelBG1.split()[0];
        torch = SpriteSheet.tileSet1.split(40, 47);

        levelBG2 = SpriteSheet.levelBG2.split()[0];
        jumpPickup = new Sprite.Builder().spriteSheet(SpriteSheet.pickupSheet).index(1).bounds(7, 7, 18, 18).build();

        boss0Moving = SpriteSheet.boss0.split(0, 3);
        boss0Idle = SpriteSheet.boss0.split(4, 7);
        for(Sprite sprite : boss0Moving) {
            sprite.setHitbox(0, 0, 108, 98);
        }

        for(Sprite sprite : boss0Idle) {
            sprite.setHitbox(0, 0, 108, 98);
        }

        fireProjectile = SpriteSheet.fireProjectile.split();
        fireProjectile[0].setHitbox(3, 29, 17, 17);
        fireProjectile[1].setHitbox(3, 29, 17, 17);
        fireProjectile[2].setHitbox(4, 29, 17, 17);
        fireProjectile[3].setHitbox(4, 29, 17, 17);

        menuBG = new Sprite(SpriteSheet.menuBG, 0);
        menuTitle = new Sprite(SpriteSheet.menuTitle, 0);
    }

    public static SpriteSheet lastSheet;

    private SpriteSheet spriteSheet;
    public int index;
    private int srcBlend = GL2.GL_SRC_ALPHA;
    private int dstBlend = GL2.GL_ONE_MINUS_SRC_ALPHA;

    private Rectangle2D.Double bounds = new Rectangle2D.Double(), hitbox = new Rectangle2D.Double();

    private Sprite(Builder builder) {
        spriteSheet = builder.spriteSheet;
        bounds = new Rectangle2D.Double(0, 0, spriteSheet.getSpriteWidth(), spriteSheet.getSpriteHeight());
        hitbox = new Rectangle2D.Double(0, 0, spriteSheet.getSpriteWidth(), spriteSheet.getSpriteHeight());
        index = builder.index;

        if(builder.bounds != null)
            bounds = builder.bounds;
        if(builder.hitbox != null)
            hitbox = builder.hitbox;
        else
            hitbox = bounds;

    }

    public Sprite(SpriteSheet sheet, int index) {
        this(new Builder().spriteSheet(sheet).index(index));
    }

    public void render(Vec2d position, boolean flipHorizontal, boolean flipVertical, double alpha, GL2 gl) {
        double texWidth = spriteSheet.getSpriteWidth();
        double texHeight = spriteSheet.getSpriteHeight();
        double numSpritesX = spriteSheet.getNumSpritesX();
        double sheetWidth = spriteSheet.getWidth();
        double sheetHeight = spriteSheet.getHeight();

        Vec2d pos = new Vec2d(position);
        pos.y = Game.HEIGHT - pos.y; //Origin in-game is top-left rather than bottom-left

        pos.plusEquals(
                flipHorizontal ? -Math.abs((hitbox.x + hitbox.width - (bounds.x + bounds.width))) : -(hitbox
                        .x - bounds.x),
                flipVertical ? -Math.abs(hitbox.y + hitbox.height - (bounds.y + bounds.width)) : (hitbox.y -
                        bounds.y)
        );

        Vec2d texTopLeft = new Vec2d(
                (index % numSpritesX) * texWidth,
                Math.floor(index / numSpritesX) * texHeight
        );

        Vec2d topLeft = new Vec2d(texTopLeft.x + bounds.x, texTopLeft.y + bounds.y);
        Vec2d topRight = new Vec2d(texTopLeft.x + bounds.x + bounds.width, texTopLeft.y + bounds.y);
        Vec2d bottomLeft = new Vec2d(texTopLeft.x + bounds.x, texTopLeft.y + bounds.y + bounds.height);
        Vec2d bottomRight = new Vec2d(texTopLeft.x + bounds.x + bounds.width, texTopLeft.y + bounds.y + bounds.height);

        Vec2d vert1 = new Vec2d(pos);
        Vec2d vert2 = pos.add(bounds.width, 0);
        Vec2d vert3 = pos.add(bounds.width, -bounds.height);
        Vec2d vert4 = pos.add(0, -bounds.height);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(srcBlend, dstBlend);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        bindSheet(gl);

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor4d(1, 1, 1, alpha);

        // Top left
        gl.glTexCoord2d(topLeft.x/sheetWidth, topLeft.y/sheetHeight);
        gl.glVertex2d(flipHorizontal ? vert2.x : vert1.x, flipVertical ? vert4.y : vert1.y);

        // Top right
        gl.glTexCoord2d(topRight.x/sheetWidth, topRight.y/sheetHeight);
        gl.glVertex2d(flipHorizontal ? vert1.x : vert2.x, flipVertical ? vert3.y : vert2.y);

        // Bottom right
        gl.glTexCoord2d(bottomRight.x/sheetWidth, bottomRight.y/sheetHeight);
        gl.glVertex2d(flipHorizontal ? vert4.x : vert3.x, flipVertical ? vert2.y : vert3.y);

        // Bottom left
        gl.glTexCoord2d(bottomLeft.x/sheetWidth, bottomLeft.y/sheetHeight);
        gl.glVertex2d(flipHorizontal ? vert3.x : vert4.x, flipVertical ? vert1.y : vert4.y);

        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_BLEND);

    }

    public void render(Vec2d position, boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        render(position, flipHorizontal, flipVertical, 1, gl);
    }

    private void bindSheet(GL2 gl) {
        if(spriteSheet != lastSheet) {
            spriteSheet.getTexture().enable(gl);
            spriteSheet.getTexture().bind(gl);
            lastSheet = spriteSheet;
        }
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    //Setters
    public void setBounds(double x, double y, double width, double height) {
        bounds = new Rectangle2D.Double(x, y, width, height);
    }

    public void setHitbox(double x, double y, double width, double height) {
        hitbox = new Rectangle2D.Double(x, y, width, height);
    }

    public void setBlendFunc(int src, int dst) {
        srcBlend = src;
        dstBlend = dst;
    }

    public static class Builder {
        private SpriteSheet spriteSheet;
        public int index;

        private Vec2d origin = new Vec2d();
        private Rectangle2D.Double bounds, hitbox;

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

        public Builder bounds(Rectangle2D.Double bounds) {
            this.bounds = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
            return this;
        }

        public Builder bounds(double x, double y, double width, double height) {
            bounds = new Rectangle2D.Double(x, y, width, height);
            return this;
        }

        public Builder hitbox(Rectangle2D.Double hitbox) {
            this.hitbox = new Rectangle2D.Double(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            return this;
        }

        public Builder hitbox(double x, double y, double width, double height) {
            hitbox = new Rectangle2D.Double(x, y, width, height);
            return this;
        }
    }
}
