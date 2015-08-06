package game.entity;


import com.jogamp.opengl.GL2;
import game.Game;
import game.collision.PolygonHitbox;
import game.collision.RectangularHitbox;
import game.event.CollisionEvent;
import game.event.EntityEvent;
import game.event.TimedEntityEvent;
import game.graphics.Animation;
import game.graphics.Sprite;
import game.level.Level;
import game.math.Vec2d;

import java.util.ArrayList;

public abstract class Entity {


    public Vec2d position, velocity, acceleration;
    public int width, height;

    protected Sprite sprite;
    protected Animation animation;

    protected RectangularHitbox hitbox;
    protected PolygonHitbox polygonHitbox = null;

    public boolean active = true;
    private boolean temporary = false;
    private boolean destroyed = false;

    private boolean collidable = false;
    private boolean collider = false;

    private boolean initiallyActive = true;
    private Vec2d initialPosition, initialVelocity;

    public boolean renderInFront = false;

    private ArrayList<EntityEvent> updateEvents = new ArrayList<>();
    private ArrayList<CollisionEvent> collisionEvents = new ArrayList<>();
    private ArrayList<TimedEntityEvent> timedEvents = new ArrayList<>();

    {
        position = new Vec2d();
        initialPosition = new Vec2d();
        velocity = new Vec2d();
        initialVelocity = new Vec2d();
        acceleration = new Vec2d();
    }

    protected Entity(Builder builder) {
        position.setEqual(builder.position);
        initialPosition.setEqual(builder.initialPosition);

        velocity.setEqual(builder.velocity);
        initialVelocity.setEqual(builder.initialVelocity);

        width = builder.width;
        height = builder.height;

        active = builder.active;
        temporary = builder.temporary;

        collider = builder.collider;
        collidable = builder.collidable;

        sprite = builder.sprite;
        animation = builder.animation;

        hitbox = new RectangularHitbox(position, width, height);

        updateEvents = builder.updateEvents;
        timedEvents = builder.timedEvents;
        collisionEvents = builder.collisionEvents;
    }

    public void update() {
        updateEvents.forEach(e -> e.activate(this));
        timedEvents.forEach(e -> e.activate(this));

        velocity.plusEquals(acceleration);
        position.plusEquals(velocity);

        hitbox.position.plusEquals(velocity);
        hitbox.updateBounds();

        if (polygonHitbox != null)
            polygonHitbox.updateBounds(velocity);
    }

    public void render(boolean flipHorizontal, boolean flipVertical, GL2 gl) {
        updateSprite();
        if(sprite == null) {
            System.out.println("Null sprite at position " + position.toString());
            return;
        }
        sprite.render(position, flipHorizontal, flipVertical, gl);
    }

    protected void updateSprite() {
        if (animation != null)
            sprite = animation.nextSprite();
    }

    public boolean collides(Entity entity) {
        if(entity == this)
            return false;
        if (hitbox.collides(entity.getHitbox())) {
            if (entity.getPolygonHitbox() != null) {
                if (polygonHitbox != null) {
                    if (polygonHitbox.collides(entity.getPolygonHitbox()))
                        return true;
                } else if (hitbox.collides(entity.getPolygonHitbox())) {
                    return true;
                }
            } else if (polygonHitbox != null) {
                if (polygonHitbox.collides(entity.getHitbox()))
                    return true;
            } else {
                return true;
            }
        }
        return false;
    }

    public void respawn() {
        if(isTemporary()) {
            destroy();
            return;
        }

        active = initiallyActive;

        Vec2d delta = initialPosition.subtract(position);
        hitbox.position.setEqual(initialPosition);
        if (polygonHitbox != null)
            polygonHitbox.updateBounds(delta);

        position.setEqual(initialPosition);
        velocity.setEqual(initialVelocity);

        timedEvents.forEach(TimedEntityEvent::restart);
    }

    public void addUpdateEvent(EntityEvent e) {
        if (e instanceof TimedEntityEvent) {
            timedEvents.add((TimedEntityEvent) e);
            return;
        }
        updateEvents.add(e);
    }

    public void addCollisionEvent(CollisionEvent e) {
        collisionEvents.add(e);
    }

    public final void collide(Entity entity) {
        collisionEvents.forEach(e -> e.collide(this, entity));
        onCollide(entity);
    }

    protected void onCollide(Entity entity) {}

    /**
     * Checks for collisions between entities in a list.
     * @param entities List of entities to be checked for collisions
     */
    public void pollCollisions(ArrayList<? extends Entity> entities) {
//      for(Entity entity : entities) {
//          if(entity.isCollidable() && entity.active && entity != this && entity.isOnScreen())
//          if(collides(entity)) {
//              entity.collide(this);
//              if(!entity.isCollider())
//                  collide(entity);
//          }
//      }
        entities.stream().filter(e -> e.active && e != this && e.isOnScreen() && e.isCollidable() && collides(e))
                .forEach(entity
                ->{
            entity.collide(this);
            if(!entity.isCollider()) collide(entity);
        });
    }

    //Setters
    public void setPosition(double x, double y) {
        if (x == position.x && y == position.y) return;

        double deltaX = x - position.x;
        double deltaY = y - position.y;

        position.setEqual(x, y);
        hitbox.position.setEqual(position);
        hitbox.updateBounds();

        if (polygonHitbox != null)
            polygonHitbox.updateBounds(deltaX, deltaY);
    }

    public void setPosition(Vec2d position) {
        setPosition(position.x, position.y);
    }

    public void updateBounds() {
        Vec2d delta = position.subtract(hitbox.position);
        hitbox.position.setEqual(position);
        hitbox.updateBounds();
        if(polygonHitbox != null)
            polygonHitbox.updateBounds(delta);
    }

    public void addToPosition(Vec2d delta) {
        setPosition(position.add(delta));
    }

    public void addToPosition(double dx, double dy) {
        setPosition(position.x + dx, position.y + dy);
    }

    public void destroy() {
        destroyed = true;
        active = false;
    }

    public void setAnimation(Animation animation) {
        if(!animation.equals(this.animation)) {
            this.animation = animation.clone();
        }
    }

    //Getters
    public RectangularHitbox getHitbox() {
        return hitbox;
    }

    public PolygonHitbox getPolygonHitbox() {
        return polygonHitbox;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isCollider() {
        return collider;
    }

    public boolean isOnScreen() {
        Vec2d camera = Game.scene.getCamera();
        int w = Game.WIDTH;
        int h = Game.HEIGHT;
        return position.x + width > camera.x && position.y + height > camera.y
                && position.x < camera.x + w && position.y < camera.y + h;
    }

    public static abstract class Builder {

        protected Vec2d position = new Vec2d(), velocity = new Vec2d();
        protected Vec2d initialPosition = new Vec2d(), initialVelocity = new Vec2d();

        protected int width, height;
        protected Sprite sprite;
        protected Animation animation;

        protected boolean active = true;
        protected boolean temporary = false;

        protected boolean collidable = false;
        protected boolean collider = false;

        protected ArrayList<EntityEvent> updateEvents = new ArrayList<>();
        protected ArrayList<TimedEntityEvent> timedEvents = new ArrayList<>();
        protected ArrayList<CollisionEvent> collisionEvents = new ArrayList<>();

        public Builder position(Vec2d position) {
            this.position.setEqual(position);
            initialPosition.setEqual(position);
            return this;
        }

        public Builder position(double x, double y) {
            position.setEqual(x, y);
            initialPosition.setEqual(position);
            return this;
        }

        public Builder velocity(Vec2d velocity) {
            this.velocity.setEqual(velocity);
            initialVelocity.setEqual(velocity);
            return this;
        }

        public Builder velocity(double x, double y) {
            velocity.setEqual(x, y);
            initialVelocity.setEqual(x, y);
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder sprite(Sprite sprite) {
            this.sprite = sprite;
            return this;
        }

        public Builder animation(Animation animation) {
            this.animation = animation.clone();
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder temporary(boolean temporary) {
            this.temporary = temporary;
            return this;
        }

        public Builder collidable(boolean collidable) {
            this.collidable = collidable;
            return this;
        }

        public Builder collider(boolean collider) {
            this.collider = collider;
            return this;
        }

        public Builder addUpdateEvent(EntityEvent event) {
            if (event instanceof TimedEntityEvent) {
                timedEvents.add((TimedEntityEvent) event);
                return this;
            }
            updateEvents.add(event);
            return this;
        }

        public Builder addCollisionEvent(CollisionEvent event) {
            collisionEvents.add(event);
            return this;
        }

        public abstract Entity build();
    }
}

