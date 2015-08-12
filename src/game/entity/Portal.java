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
        Vec2d prevPlayerPos = new Vec2d(player.position);

        float width = (float) this.width;
        float height = (float) this.height;

        if(direction == Direction.RIGHT) {
            alpha = maxAlpha * (float) Math.abs(position.x - player.position.x) / (width);
        } else {
            alpha = maxAlpha * (float) Math.abs(position.x - width + player.position.x) / (width/2);
        }

        Vec2d previousCamera = new Vec2d(Game.screen.getCamera());
        Game.screen.setCamera(destPortal.position);

        FrameBuffer.portal.bind(gl);
        gl.glLoadIdentity();
        gl.glTranslated(Game.screen.getCamera().x, Game.screen.getCamera().y, 0);
        player.setPosition(player.position.subtract(position).add(destPortal.position));
        destination.render(gl);
        FrameBuffer.unbindCurrentFramebuffer(gl);

        player.setPosition(prevPlayerPos);

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
        gl.glVertex2d(position.x, 608 - (position.y + height)); // bottom left

        gl.glTexCoord2f(0, 0);
        if(direction == Direction.RIGHT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(position.x, 608 - position.y); // top left

        gl.glTexCoord2f(width/Game.WIDTH, 0);
        if(direction == Direction.LEFT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(position.x + width, 608 - position.y); // top right

        gl.glTexCoord2f(width/Game.WIDTH, (-height)/Game.HEIGHT);
        if(direction == Direction.LEFT)
            gl.glColor4f(1, 1, 1, 0);
        else
            gl.glColor4f(1, 1, 1, alpha);
        gl.glVertex2d(position.x + width, 608 - (position.y + height)); // bottom right
        gl.glEnd();
        FrameBuffer.portal.unbindTexture(gl);
        gl.glDisable(GL2.GL_BLEND);

    }

    public void onCollide(Entity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Vec2d newPosition = player.position.subtract(position).add(destPortal.position);

            if(direction == Direction.RIGHT) {
                if (player.position.x > position.x + width / 2) {
                    Level.setLevel(destination);
                    player.setPosition(newPosition);
                }
            }
            else if(player.position.x < position.x + width/2) {
                Level.setLevel(destination);
                player.setPosition(newPosition);
            }

            if(!destination.music.playing()) {
                destination.music.play(true, 0);
            } else if(!destination.music.done()) {
                destination.music.resume();
            }

            switch(direction) {
                case RIGHT:
                    destination.music.setVolume(
                            Math.min(destination.musicVolume,
                                     Math.abs(player.position.x - position.x) / width * destination.musicVolume)
                    );
                    Level.getCurrentLevel().music.setVolume(
                            Math.min(Level.getCurrentLevel().musicVolume,
                                    Math.abs(position.x + width - player.position.x) / width
                                            * Level.getCurrentLevel().musicVolume)
                    );
                    break;
                case LEFT:
                    destination.music.setVolume(
                            Math.min(destination.musicVolume,
                                    Math.abs(position.x + width - player.position.x) / width * destination.musicVolume)
                    );
                    Level.getCurrentLevel().music.setVolume(
                            Math.min(Level.getCurrentLevel().musicVolume,
                                    Math.abs(player.position.x - position.x) / width * Level.getCurrentLevel().musicVolume)
                    );
            }
            if(destination.music.getVolume() / destination.musicVolume < 0.005 && destination.music.playing()) {
                destination.music.pause();
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
