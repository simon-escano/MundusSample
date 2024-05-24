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
        stateTime += Gdx.graphics.getDeltaTime();
        setDecal();
        if (stateTime > animationSpeed) {
            spriteCtr++;
            if (spriteCtr > 4) {
                spriteCtr = 1;
            }
            stateTime = 0f;
        }
        direction = getRelativeDirection(MyGdxGame.mainPlayer.getPosition());
        setPosition(getPosition());

//        chase(MyGdxGame.mainPlayer.getPosition());

        MyGdxGame.decalBatch.add(getDecal());
        DecalHelper.applyLighting(getDecal(), MyGdxGame.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(getDecal(), MyGdxGame.scene.cam);
    }

    public void chase(Vector3 player) {
        if (getPosition().dst(player) < 60) {
            setPosition(getPosition().x, getY(), getPosition().z);
            return;
        }

        if (getPosition().x < player.x) {
            getPosition().x += velocity;
        } else if (getPosition().x > player.x) {
            getPosition().x -= velocity;
        }

        if (getPosition().z < player.z) {
            getPosition().z += velocity;
        } else if (getPosition().z > player.z) {
            getPosition().z -= velocity;
        }

        setPosition(getPosition().x, getY(), getPosition().z);
    }
}