package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;

public class MainPlayer extends Player {
    private final FirstPersonCameraController controller;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 5;
    TextureRegion[][] sprites;
    public int spriteCtr = 1;
    Decal currentSprite;
    public float stateTime = 0f;
    public float animationSpeed = 0.1f;

    public MainPlayer(String spriteSheetPath, float velocity, Camera camera) {
        super(spriteSheetPath, 0, 0, velocity);
        position = camera.position;
        controller = new FirstPersonCameraController(camera);
        controller.setVelocity(velocity);
        Gdx.input.setInputProcessor(controller);

        Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/" + spriteSheetPath));
        sprites = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
    }

    public void update(DecalBatch decalBatch) {

        if (getDirection() == null) {
            state = State.IDLE;
        } else {
            direction = getDirection();
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                state = State.SPRINTING;
                animationSpeed = 0.025f;
                setVelocity(200f);
            } else {
                if (getVelocity() != 100f) {
                    setVelocity(100f);
                }
                state = State.WALKING;
                animationSpeed = 0.1f;
            }
        }

        switch (state) {
            case IDLE:
                spriteCtr = 0;
                break;
            case SPRINTING:
            case WALKING:
                stateTime += Gdx.graphics.getDeltaTime();
                if (stateTime > animationSpeed) {
                    spriteCtr++;
                    if (spriteCtr > 4) {
                        spriteCtr = 1;
                    }
                    stateTime = 0f;
                }
                break;
        }

        float height = MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z + 50, new Matrix4()) + DecalHelper.offset(40);
        currentSprite = Decal.newDecal(40, 40, sprites[spriteCtr][direction.ordinal()], true);
        currentSprite.setPosition(position.x, height, position.z + 50);
        currentSprite.setRotationY(-180);
        position.y = Math.max(MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z, new Matrix4()), height + 20);
        decalBatch.add(currentSprite);
        controller.update();
    }

    public Decal getDecal() {
        return currentSprite;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
        controller.setVelocity(velocity);
    }

    public float getVelocity() {
        return velocity;
    }

    public Direction getDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direction.NORTHEAST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direction.NORTHWEST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direction.SOUTHEAST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direction.SOUTHWEST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Direction.NORTH;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direction.WEST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Direction.SOUTH;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direction.EAST;
        }
        return null;
    }
}