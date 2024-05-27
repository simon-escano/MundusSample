package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;

public abstract class Entity {
    public int FRAME_COLS, FRAME_ROWS;

    public enum Direction implements Serializable {
        S, SW, W, NW, N, NE, E, SE
    }
    public enum State implements Serializable {
        IDLE, WALKING, SPRINTING
    }
    protected Direction direction;
    protected State state;
    protected Vector3 position;
    protected TextureRegion[][] sprites;
    protected Decal decal;
    protected int spriteWidth;
    protected int spriteHeight;
    protected float velocity;

    protected int spriteCtr = 0;
    protected float stateTime = 0f;
    protected float animationSpeed = 0.1f;

    public Entity(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity) {
        direction = Direction.N;
        state = State.IDLE;
        position = new Vector3();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.velocity = velocity;
        setSprites(spriteSheetPath);
        setDecal();
    }

    public Entity(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity, int FRAME_ROWS, int FRAME_COLS) {
        direction = Direction.N;
        state = State.IDLE;
        position = new Vector3();
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.velocity = velocity;
        setFrames(FRAME_ROWS, FRAME_COLS);
        setSprites(spriteSheetPath);
        setDecal();
    }

    public void setFrames(int row, int col) {
        FRAME_ROWS = row;
        FRAME_COLS = col;
    }

    public abstract void update();

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setState(State state) {
        this.state = state;
    }

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

        if (FRAME_ROWS == 0 || FRAME_COLS == 0) {
            System.out.println("NULL FRAMES");
            setFrames(5, 8);
        }
        Texture spriteSheet = new Texture(Gdx.files.internal(spriteSheetPath));
        sprites = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
    }

    public void setDecal() {
        decal = Decal.newDecal(spriteWidth, spriteHeight, sprites[spriteCtr][getSpriteDirection().ordinal()], true);
    }

    public Decal getDecal() {
        return decal;
    }

    public float getY() {
        return getY(position.x, position.z);
    }

    public float getY(float x, float z) {
        return Utils.getHeight(x, z) + DecalHelper.offset(spriteHeight);
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public Direction getDirectionToCamera() {
        float deltaX = MyGdxGame.scene.cam.position.x - position.x;
        float deltaZ = MyGdxGame.scene.cam.position.z - position.z;
        return Utils.vectorToDirection(deltaX, deltaZ);
    }

    public Direction getSpriteDirection() {
        Direction directionToCamera = getDirectionToCamera();
        Direction[] directions = Direction.values();
        int i = direction.ordinal() - directionToCamera.ordinal();
        if (i < 0) {
            i += 8;
        }
        return directions[i];
    }
}
