package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity {
    public enum Direction {
        SOUTH, SOUTHWEST, WEST, NORTHWEST, NORTH, NORTHEAST, EAST, SOUTHEAST
    }
    public enum State {
        IDLE, WALKING, SPRINTING
    }
    public Direction direction;
    public State state;
    public Vector3 position;
    public TextureRegion textureRegion;
    public float velocity;
    public Entity(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity) {
        textureRegion = new TextureRegion(new Texture("spritesheets/" + spriteSheetPath), 0, 0, spriteWidth, spriteHeight);
        direction = Direction.SOUTH;
        state = State.IDLE;
        position = new Vector3();
        this.velocity = velocity;
    }

    public abstract void update();
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
}
