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
    TextureRegion[][] sprites;
    public int spriteCtr = 1;
    public float stateTime = 0f;
    public float animationSpeed = 0.1f;

    public MainPlayer(float velocity) {
        super("", 0, 0, velocity);
        setPosition(MyGdxGame.scene.cam.position);
        controller = new FirstPersonCameraController(MyGdxGame.scene.cam);
        controller.setVelocity(velocity);
        Gdx.input.setInputProcessor(controller);

//        Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/" + spriteSheetPath));
//        sprites = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / FRAME_COLS, spriteSheet.getHeight() / FRAME_ROWS);
    }

    public void update() {
//
//        if (getDirection() == null) {
//            state = State.IDLE;
//        } else {
//            direction = getDirection();
//            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
//                state = State.SPRINTING;
//                animationSpeed = 0.025f;
//                setVelocity(200f);
//            } else {
//                if (getVelocity() != 100f) {
//                    setVelocity(100f);
//                }
//                state = State.WALKING;
//                animationSpeed = 0.1f;
//            }
//        }
//
//        switch (state) {
//            case IDLE:
//                spriteCtr = 0;
//                break;
//            case SPRINTING:
//            case WALKING:
//                stateTime += Gdx.graphics.getDeltaTime();
//                if (stateTime > animationSpeed) {
//                    spriteCtr++;
//                    if (spriteCtr > 4) {
//                        spriteCtr = 1;
//                    }
//                    stateTime = 0f;
//                }
//                break;
//        }
//
//        float height = MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z + 50, new Matrix4()) + DecalHelper.offset(40);
//        decal = Decal.newDecal(40, 40, sprites[spriteCtr][direction.ordinal()], true);
//        decal.setPosition(position.x, height, position.z + 50);
//        decal.setRotationY(-180);
//        position.y = Math.max(MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z, new Matrix4()), height + 20);
//        decalBatch.add(decal);
        getPosition().y = MyGdxGame.terrain.getHeightAtWorldCoord(getPosition().x, getPosition().z, new Matrix4());
        controller.update();
    }

    @Override
    public void setVelocity(float velocity) {
        super.setVelocity(velocity);
        controller.setVelocity(velocity);
    }
}