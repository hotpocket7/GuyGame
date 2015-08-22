package game.level;

import game.entity.Entity;
import game.entity.JumpPickup;
import game.entity.Portal;
import game.event.EntityBounceEvent;
import game.event.Events;
import game.event.TimedEntityEvent;
import game.graphics.Animation;
import game.graphics.Color4f;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.graphics.light.ConeLight;
import game.graphics.light.RadialLight;
import game.level.block.*;
import game.math.Vec2d;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LevelLoader {

    private String path;
    private Level level;

    public LevelLoader(String path, Level level) {
        this.path = path;
        this.level = level;
    }

    //Levels are stored in JSON files made with Tiled
    public void load() throws IOException, JSONException {
        String levelString = IOUtils.toString(Level.class.getResourceAsStream(path));

        JSONObject levelObj = new JSONObject(levelString);
        JSONArray layers = levelObj.getJSONArray("layers");

        loadLayers(layers);
    }

    private void loadLayers(JSONArray layers) throws JSONException {
        for (int i = 0; i < layers.length(); i++) {
            JSONObject layer = layers.getJSONObject(i);
            loadLayer(layer);
        }
    }

    private void handleProperties(JSONObject properties, Entity.Builder builder) throws JSONException {
        if (properties.has("moving")) {
            if (properties.getString("moving").equals("")) {
                Vec2d velocity = new Vec2d(
                        properties.has("xVel") ? properties.getDouble("xVel") : 0,
                        properties.has("yVel") ? properties.getDouble("yVel") : 0
                );
                builder.velocity(velocity);
                double xMin = properties.has("xMin") ? properties.getDouble("xMin") : 0;
                double xMax = properties.has("xMax") ? properties.getDouble("xMax") : 0;
                double yMin = properties.has("yMin") ? properties.getDouble("yMin") : 0;
                double yMax = properties.has("yMax") ? properties.getDouble("yMax") : 0;
                builder.addUpdateEvent(new EntityBounceEvent(
                        new Vec2d(xMax, yMax),
                        new Vec2d(xMin, yMin)
                ));
            }
        }
    }

    private void loadLayer(JSONObject layer) throws JSONException {
        JSONObject layerProperties = layer.getJSONObject("properties");
        String type = layerProperties.getString("type");
        String tileSetName = layerProperties.getString("tileset");
        SpriteSheet tileSet = null;

        JSONArray entities = layer.getJSONArray("objects");

        switch (tileSetName) {
            case "block1":
                tileSet = SpriteSheet.tileSet1;
                break;
            case "block2":
                tileSet = SpriteSheet.tileSet2;
                break;
            case "pickup1":
                tileSet = SpriteSheet.pickupSheet;
        }

        for (int index = 0; index < entities.length(); index++) {
            JSONObject currentEntity = entities.getJSONObject(index);
            JSONObject properties = currentEntity.getJSONObject("properties");

            int x = currentEntity.getInt("x");
            int y = currentEntity.getInt("y") - 32;
            Vec2d position = new Vec2d(x, y);

            int id = (currentEntity.getInt("gid") - 1) % 64;
            Sprite sprite = new Sprite(tileSet, id);

            Entity.Builder builder = null;
            if (type.equals("block")) {
                if (tileSet == SpriteSheet.tileSet1) {
                    //Castle tileset
                    switch (id) {
                        case 1:
                            builder = new BlockGeneric.Builder().special(true).position(position).sprite(sprite)
                                    .collidable(true)
                                    .addCollisionEvent(Events.killPlayerOnTouch);
                            break;
                        case 2:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.UP).position(position)
                                    .sprite(sprite);
                            break;
                        case 3:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.RIGHT).position(position)
                                    .sprite(sprite);
                            break;
                        case 4:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.DOWN).position(position)
                                    .sprite(sprite);
                            break;
                        case 5:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.LEFT).position(position)
                                    .sprite(sprite);
                            break;
                        case 6:
                        case 7:
                            builder = new BlockSave.Builder().normalSprite(new Sprite(tileSet, 6))
                                    .savedSprite(new Sprite(tileSet, 7)).position(position).sprite(sprite);
                            break;
                        case 8:
                            level.setSpawn(position);
                            break;
                        case 11:
                            builder = new BlockGeneric.Builder().solid(false).position(position)
                                    .sprite(sprite).animation(Animation.torch);
                            level.addLight(new RadialLight(position.add(16, 8), 250,
                                    new Color4f(242 / 255f, 90 / 255f, 7 / 255f, 0.6f), level.ambientColor));
                            break;
                        case 12:
                            builder = new BlockGeneric.Builder().special(true)
                                    .solidTop(true).solidBottom(false).solidLeft(false).solidRight(false)
                                    .collidable(true).size(32, 16).position(position).sprite(sprite);
                            break;
                        case 16:case 21:
                        case 24:case 29:
                        case 32:case 37:
                            builder = new BlockGeneric.Builder().solid(false).position(position).sprite(sprite);
                            break;
                        case 48:
                            int destination = properties.getInt("destination");
                            System.out.println(destination);
                            String destPortal = properties.getString("destPortal");
                            int width = properties.getInt("portalWidth"), height = properties.getInt("portalHeight");
                            double maxAlpha = properties.getDouble("maxAlpha");

                            Portal.Direction direction = Portal.Direction.valueOf(
                                    properties.getString("direction").toUpperCase()
                            );

                            builder = new Portal.Builder()
                                    .direction(direction).destination(destination).destPortal(destPortal)
                                    .maxAlpha((float) maxAlpha)
                                    .sprite(null).collidable(true)
                                    .size(width, height).position(position);
                            break;
                        default:
                            builder = new BlockGeneric.Builder().collidable(true).position(position).sprite(sprite);
                    }
                } else if (tileSet == SpriteSheet.tileSet2) {
                    switch (id) {
                        case 5:
                            level.setSpawn(position);
                            break;
                        case 6:
                        case 7:
                            builder = new BlockSave.Builder().lightIntensity(0.95f).normalSprite(new Sprite(tileSet, 6))
                                    .savedSprite(new Sprite(tileSet, 7)).position(position);
                            break;
                        case 14: {
                            builder = new BlockGeneric.Builder().solid(false)
                                    .position(position).size(16, 16).sprite(sprite);
                            int delay = properties.getInt("delay");
                            builder.addUpdateEvent(new TimedEntityEvent(
                                    delay,
                                    (b) -> {
                                        Entity.Builder builder1 = new JumpPickup.Builder()
                                                .position(position.add(b.width, -Math.abs(b.height - JumpPickup.DEFAULT_HEIGHT) / 2f))
                                                .velocity(2, 0)
                                                .sprite(new Sprite(SpriteSheet.pickupSheet, 1, -8, 8, JumpPickup.DEFAULT_WIDTH, JumpPickup.DEFAULT_HEIGHT))
                                                .temporary(true)
                                                .collider(true)
                                                .addUpdateEvent(Events.destroyOnLeaveScreen).updateOffScreen(true)
                                                .addCollisionEvent(Events.destroyOnTouchBlock);
                                        Level.getCurrentLevel().addEntity(builder1.build());
                                    }
                            ));
                        }
                        break;
                        case 15: {
                            builder = new BlockGeneric.Builder().solid(false)
                                    .position(position).size(16, 16).sprite(sprite);
                            int delay = properties.getInt("delay");
                            builder.addUpdateEvent(new TimedEntityEvent(
                                    delay,
                                    (b) -> {
                                        Entity.Builder builder1 = new JumpPickup.Builder()
                                                .position(position.add(16 - b.width, -Math.abs(b.height - JumpPickup.DEFAULT_HEIGHT) / 2f))
                                                .velocity(-2, 0)
                                                .sprite(new Sprite(SpriteSheet.pickupSheet, 1, -8, 8, JumpPickup.DEFAULT_WIDTH, JumpPickup.DEFAULT_HEIGHT))
                                                .temporary(true)
                                                .collider(true)
                                                .addUpdateEvent(Events.destroyOnLeaveScreen).updateOffScreen(true)
                                                .addCollisionEvent(Events.destroyOnTouchBlock);
                                        Level.getCurrentLevel().addEntity(builder1.build());
                                    }
                            ));
                        } break;

                        case 19:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.UP).position(position)
                                    .sprite(sprite).collidable(true);
                            break;
                        case 20:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.RIGHT).position(position)
                                    .sprite(sprite).collidable(true);
                            break;
                        case 21:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.DOWN).position(position)
                                    .sprite(sprite).collidable(true);
                            break;
                        case 22:
                            builder = new BlockSpike.Builder().direction(BlockSpike.Direction.LEFT).position(position)
                                    .sprite(sprite).collidable(true);
                            break;
                        case 23: {
                            builder = new BlockGeneric.Builder().position(position).size(16, 16).sprite(sprite);
                            int delay = properties.getInt("delay");
                            builder.addUpdateEvent(new TimedEntityEvent(
                                    delay,
                                    (b) -> {
                                        Entity.Builder builder1 = new JumpPickup.Builder()
                                                .position(position.add(-1, 16))
                                                .velocity(0, 2)
                                                .sprite(new Sprite(SpriteSheet.pickupSheet, 1, -8, 8, JumpPickup.DEFAULT_WIDTH, JumpPickup.DEFAULT_HEIGHT))
                                                .temporary(true)
                                                .collider(true)
                                                .addUpdateEvent(Events.destroyOnLeaveScreen).updateOffScreen(true)
                                                .addCollisionEvent(Events.destroyOnTouchBlock);
                                        Level.getCurrentLevel().addEntity(builder1.build());
                                    }
                            ));
                        }
                            break;
                        case 24: {
                            builder = new BlockGeneric.Builder().solid(false).position(position).sprite(sprite);
                            Color4f lightColor = new Color4f(1, 1, 1, 1);
                            level.addLight(new ConeLight(position.add(16.5, 24), 400,
                                    (float) Math.PI / 4, 3 * (float) Math.PI / 4f,
                                    lightColor, level.ambientColor));
                            break;
                        }
                        case 25: {
                            Color4f lightColor = new Color4f(1, 1, 1, 0.9f);
                            level.addLight(new RadialLight(position.add(32, 3), 500, 300, lightColor, level
                                    .ambientColor));
                        }
                        case 3:case 4:case 9:case 11:case 12:case 35:case 36:case 43:case 44:
                        case 26:
                            builder = new BlockGeneric.Builder().solid(false).position(position).sprite(sprite);
                            break;
                        case 31: {
                            builder = new BlockGeneric.Builder().position(position).size(16, 16).sprite(sprite);
                            int delay = properties.getInt("delay");
                            builder.addUpdateEvent(new TimedEntityEvent(
                                    delay,
                                    (b) -> {
                                        Entity.Builder builder1 = new JumpPickup.Builder()
                                                .position(position.add(-1, -JumpPickup.DEFAULT_HEIGHT))
                                                .velocity(0, -2)
                                                .sprite(new Sprite(SpriteSheet.pickupSheet, 1, -8, 8, JumpPickup.DEFAULT_WIDTH, JumpPickup.DEFAULT_HEIGHT))
                                                .temporary(true)
                                                .collider(true)
                                                .addUpdateEvent(Events.destroyOnLeaveScreen).updateOffScreen(true)
                                                .addCollisionEvent(Events.destroyOnTouchBlock);
                                        Level.getCurrentLevel().addEntity(builder1.build());
                                    }
                            ));
                        }
                        break;
                        case 32:
                            builder = new BlockGeneric.Builder().special(true)
                                    .solidTop(true).solidBottom(false).solidLeft(false).solidRight(false)
                                    .collidable(true).size(32, 16).position(position).sprite(sprite);
                            break;
                        case 39:
                            int destination = properties.getInt("destination");
                            String destPortal = properties.getString("destPortal");
                            int width = properties.getInt("portalWidth"), height = properties.getInt("portalHeight");
                            double maxAlpha = properties.getDouble("maxAlpha");
                            Portal.Direction direction = Portal.Direction.valueOf(
                                    properties.getString("direction").toUpperCase()
                            );

                            builder = new Portal.Builder()
                                    .direction(direction).destination(destination).destPortal(destPortal)
                                    .maxAlpha((float) maxAlpha)
                                    .sprite(null).collidable(true)
                                    .width(width).height(height).position(position);
                            break;
                        default:
                            builder = new BlockGeneric.Builder().collidable(true).position(position).sprite(sprite)
                                    .size(32, 32);
                    }
                }

            } else if (type.equals("pickup")) {
                switch (id) {
                    case 1:
                        sprite = new Sprite(tileSet, id, -8, 8, 18, 18);
                        builder = new JumpPickup.Builder().position(position.add(8, 8)).sprite(sprite);
                        break;
                    default:
                }
            }
            if (builder == null)
                continue;

            handleProperties(properties, builder);
            Entity entity = builder.build();
            level.addEntity(entity);

            String name = currentEntity.getString("name");
            if (!name.equals(""))
                level.entityMap.put(name, entity);
        }
    }
}
