package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Player extends Entity {

    public Player(ServerPlayer serverPlayer) {
        super("astronaut_spritesheet_" + serverPlayer.getColor() + ".png", 100, 100, 0.5f);
        position = serverPlayer.getPosition();
        direction = serverPlayer.getDirection();
        state = serverPlayer.getState();
    }

    public Player() {
        super("", 0, 0, 0);
    }

    public void update() {
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

        MyGdxGame.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, MyGdxGame.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, MyGdxGame.scene.cam);
    }
}