package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mbrlabs.mundus.commons.terrain.Terrain;

import java.util.Random;
import java.util.Vector;

public class Monster extends Entity {
    public Monster(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity) {
        super(spriteSheetPath, spriteWidth, spriteHeight, velocity);
        Random random = new Random();
        setPosition(100, getY(100, 200), 200);
    }

    @Override
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

        setDecal();
        setPosition(position);

//        chase(MyGdxGame.mainPlayer.getPosition());

        MyGdxGame.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, MyGdxGame.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, MyGdxGame.scene.cam);
    }

    public void chase(Vector3 player) {
        if (position.dst(player) < 60) {
            setPosition(position.x, getY(), position.z);
            return;
        }

        if (position.x < player.x) {
            position.x += velocity;
        } else if (position.x > player.x) {
            position.x -= velocity;
        }

        if (position.z < player.z) {
            position.z += velocity;
        } else if (position.z > player.z) {
            position.z -= velocity;
        }

        setPosition(position.x, getY(), position.z);
    }
}