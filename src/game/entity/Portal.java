package game.entity;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.FrameBuffer;
import game.level.Level;
import game.math.Vec2d;

public class Portal extends Entity {

    public enum Direction {
        LEFT, RIGHT
    }

    private Direction direction;

    private int destinationID;
    private Level destination = null;
    private String destPortalName;
    private Entity destPortal;

    private float alpha = 1f;
    private float maxAlpha = 1f;

    private Portal(Builder builder) {
        super(builder);
        direction = builder.direction;
        destinationID = builder.destinationID;
        destPortalName = builder.destPortalName;
        maxAlpha = builder.maxAlpha;
        renderInFront = true;
    }

    public void render(boolean flipH, boolean flipV, GL2 gl) {
        if(Level.levels.size() + 1 > destinationID) {
            destination = Level.levels.get(destinationID);
        }
        if(destination == null) {
            System.err.println("Null portal destination " + destination);
            return;
        }
        if (Level.getCurrentLevel() == destination) {
            return;
        }

        if (destination.entityMap.containsKey(destPortalName)) {
            destPortal = destination.entityMap.get(destPortalName);
        } else {
            System.out.printf("Portal does not exist: %s\n", destPortalName);
            return;
        }

        Player player = Game.screen.getPlayer();
        Vec2d prevPlayerPos = player.getPos();

        float width = (float) this.width;
        float height = (float) this.height;

        if(direction == Direction.RIGHT) {
            alpha = maxAlpha * (float) Math.abs(getXPos() - player.getXPos()) / (width);
        } else {
            alpha = maxAlpha * (float) Math.abs(getXPos() - width + player.getXPos()) / (width/2);
        }

        Vec2d previousCamera = new Vec2d(Game.screen.getCamera());
        Game.screen.setCamera(destPortal.getPos());

        FrameBuffer.portal.bind(gl);
        gl.glLoadIdentity();
        gl.glTranslated(Game.screen.getCamera().x, Game.screen.getCamera().y, 0);
        player.setPos(player.getPos().subtract(getPos()).add(destPortal.getPos()));
        destination.render(gl);
        FrameBuffer.unbindCurrentFramebuffer(gl);

        player.setPos(prevPlayerPos);

        Game.screen.setCamera(previousCamera);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        FrameBuffer.portal.bindTexture(gl);
        gl.glLoadIdentity();
        gl.glTranslated(-Game.screen.getCamera().x, Game.screen.getCamera().y, 0);


        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(0, (-height)/Game.HEIGHT);
        if(direction == Direction.RIGHT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(getXPos(), 608 - (getYPos() + height)); // bottom left

        gl.glTexCoord2f(0, 0);
        if(direction == Direction.RIGHT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(getXPos(), 608 - getYPos()); // top left

        gl.glTexCoord2f(width/Game.WIDTH, 0);
        if(direction == Direction.LEFT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(getXPos() + width, 608 - getYPos()); // top right

        gl.glTexCoord2f(width/Game.WIDTH, (-height)/Game.HEIGHT);
        if(direction == Direction.LEFT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(getXPos() + width, 608 - (getYPos() + height)); // bottom right
        gl.glEnd();
        FrameBuffer.portal.unbindTexture(gl);
        gl.glDisable(GL2.GL_BLEND);

    }

    public void onCollide(Entity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Vec2d newPosition = player.getPos().subtract(getPos()).add(destPortal.getPos());

            if(direction == Direction.RIGHT) {
                if (player.getXPos() > getXPos() + width / 2) {
                    Level.setLevel(destination);
                    player.setPos(newPosition);
                }
            }
            else if(player.getXPos() < getXPos() + width/2) {
                Level.setLevel(destination);
                player.setPos(newPosition);
            }

            if(!destination.song.playing()) {
                destination.song.play(true, 0);
            } else if(!destination.song.done()) {
                destination.song.resume();
            }

            switch(direction) {
                case RIGHT:
                    destination.song.setVolume(
                            Math.min(destination.song.getMaxVolume(),
                                     Math.abs(player.getXPos() - getXPos()) / width * destination.song.getMaxVolume())
                    );
                    Level.getCurrentLevel().song.setVolume(
                            Math.min(Level.getCurrentLevel().song.getMaxVolume(),
                                    Math.abs(getXPos() + width - player.getXPos()) / width
                                            * Level.getCurrentLevel().song.getMaxVolume())
                    );
                    break;
                case LEFT:
                    destination.song.setVolume(
                            Math.min(destination.song.getMaxVolume(),
                                    Math.abs(getXPos() + width - player.getXPos()) / width * destination.song.getMaxVolume())
                    );
                    Level.getCurrentLevel().song.setVolume(
                            Math.min(Level.getCurrentLevel().song.getMaxVolume(),
                                    Math.abs(player.getXPos() - getXPos()) / width * Level.getCurrentLevel()
                                            .song.getMaxVolume())
                    );
            }
            if(destination.song.getVolume() / destination.song.getMaxVolume() < 0.005 && destination.song.playing()) {
                destination.song.pause();
            }
        }
    }

    public static class Builder extends Entity.Builder {

        private Direction direction;
        private int destinationID;
        private String destPortalName;
        private float maxAlpha;

        public Entity build() {
            return new Portal(this);
        }

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder destination(int destinationID) {
            this.destinationID = destinationID;
            return this;
        }

        public Builder destPortal(String name) {
            destPortalName = name;
            return this;
        }

        public Builder maxAlpha(float maxAlpha) {
            this.maxAlpha = maxAlpha;
            return this;
        }
    }
}
