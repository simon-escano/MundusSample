package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.w3c.dom.Text;

import java.io.IOException;

public abstract class Entity {
    private static final int FRAME_COLS = 8, FRAME_ROWS = 5;
    public enum Direction {
        SOUTH, SOUTHWEST, WEST, NORTHWEST, NORTH, NORTHEAST, EAST, SOUTHEAST
    }
    public enum State {
        IDLE, WALKING, SPRINTING
    }
    public Direction direction;
    public State state;
    private Vector3 position;
    public TextureRegion[][] sprites;
    private Decal decal;
    public int spriteWidth;
    public int spriteHeight;
    public float velocity;

    int spriteCtr = 0;
    float stateTime = 0f;
    float animationSpeed = 0.1f;

    public Entity(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity) {
        direction = Direction.SOUTH;
        state = State.IDLE;
        position = new Vector3();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.velocity = velocity;
        setSprites(spriteSheetPath);
        setDecal();
    }

    public abstract void update();

    public void setPosition(Vector3 position) {
        this.position = position;
        decal.setPosition(position);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        decal.setPosition(position);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setSprites(String spriteSheetPath) {
        if (spriteSheetPath.isEmpty()) {
            spriteSheetPath = "badlogic.jpg";
        }
        Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/" + spriteSheetPath));
        sprites = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
    }

    public void setDecal() {
        decal = Decal.newDecal(spriteWidth, spriteHeight, sprites[spriteCtr][direction.ordinal()], true);
    }

    public Decal getDecal() {
        return decal;
    }

    public float getY() {
        return getY(position.x, position.z);
    }

    public float getY(float x, float z) {
        return MyGdxGame.terrain.getHeightAtWorldCoord(x, z, new Matrix4()) + DecalHelper.offset(spriteHeight);
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public Direction getRelativeDirection(Vector3 target) {
        Vector3 delta = new Vector3(target).sub(position);
        float angle = (float) Math.toDegrees(Math.atan2(delta.z, delta.x));

        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }

        if (-22.5 <= angle && angle < 22.5 || angle >= 337.5) {
            return Direction.NORTH;
        } else if (22.5 <= angle && angle < 67.5) {
            return Direction.NORTHEAST;
        } else if (67.5 <= angle && angle < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= angle && angle < 157.5) {
            return Direction.SOUTHEAST;
        } else if (157.5 <= angle && angle < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= angle && angle < 247.5) {
            return Direction.SOUTHWEST;
        } else if (247.5 <= angle && angle < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= angle && angle < 337.5) {
            return Direction.NORTHWEST;
        }
        return null;
    }
}
